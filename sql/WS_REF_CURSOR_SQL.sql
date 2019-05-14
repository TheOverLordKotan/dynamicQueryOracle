create or replace PACKAGE WS_REF_CURSOR_SQL AS
  --
  -- ReÃºne procedimientos directamente relacionados con retiro .....
  --
  -- #VERSION:0000001000
  --
  -- HISTORIAL DE CAMBIOS
  --
  -- VersiÃ³n     GAP           Solicitud        Fecha        RealizÃ³        DescripciÃ³n
  -- =========== ============= ================ ============ ============== ==============================================================================
  -- 1000        THEOVERLORD                    04/03/2019   *ADA          . Version inicial
  -- =========== ============= ================ ============ ============== ==============================================================================
  --
-------------------------------------------------------------------------------------------------
--Type
-------------------------------------------------------------------------------------------------


-------------------------------------------------------------------------------------------------
-- Constantes generales
-------------------------------------------------------------------------------------------------
TRANS_CONSUL_PRODU        VARCHAR2(4) := 'ANRE';  --Constante para identificar la transaccion de CONSUL
COD_TRANS_FAI               varchar2(4) := 'FAIL';
FORMATO_FECHA_SIFI          varchar2(8)     :='ddmmyyyy'; -- Formato de fecha para todas las transacciones
--
FORMATO_FECHA_EXT           varchar2(10)    :='YYYY-MM-DD';  -- Formato de fecha de salida para el Sistema Externo


  TYPE cursorType IS REF CURSOR;

  FUNCTION GET_OBJECTS_BY_NAME(I_xml IN CLOB)
  RETURN cursorType;
  
  FUNCTION GET_OBJECTS_BY_OWNER(I_xml IN CLOB)
  RETURN cursorType;

END WS_REF_CURSOR_SQL;
/

create or replace PACKAGE BODY WS_REF_CURSOR_SQL AS

-------------------------------------------------------------------------------
--  Function Name  :  GET_OBJECTS_BY_NAME
-------------------------------------------------------------------------------
FUNCTION GET_OBJECTS_BY_NAME(I_xml IN  CLOB)
RETURN cursorType AS 
    
    L_cursor    cursorType;
      v_ReturnCursor cursorType;
      v_SQLStatement VARCHAR2(4000);
      v_result VARCHAR2(4000);
    BEGIN
      v_SQLStatement := I_xml;

      IF(UPPER( v_SQLStatement ) LIKE '%SELECT%') THEN 
        OPEN v_ReturnCursor FOR v_SQLStatement;
        RETURN v_ReturnCursor;
    
      ELSE
        EXECUTE IMMEDIATE v_SQLStatement;
        OPEN v_ReturnCursor FOR
        SELECT 'OK' as STATUS
        FROM DUAL;

        RETURN v_ReturnCursor;
      END IF;
    EXCEPTION
    WHEN OTHERS THEN
        v_result:= SUBSTR('CODE'|| SQLERRM ,0,3991);
        OPEN v_ReturnCursor FOR
        SELECT 'ERROR' as STATUS ,v_result as code
        FROM DUAL;

        RETURN v_ReturnCursor;

END GET_OBJECTS_BY_NAME;


-------------------------------------------------------------------------------
--  Function Name  :  GET_OBJECTS_BY_OWNER
-------------------------------------------------------------------------------
FUNCTION GET_OBJECTS_BY_OWNER(I_xml IN  CLOB)
RETURN cursorType AS 

    L_cursor    cursorType;

BEGIN
    OPEN L_cursor FOR
        SELECT owner,
               object_name,
               object_type,
               status 
          FROM all_objects
         WHERE owner IN (SELECT xmlt.object_owner
                           FROM XMLTABLE('/input/row'
                                         PASSING xmltype(I_xml)
                                         COLUMNS
                                         object_owner     VARCHAR2(25)    PATH './objectOwner'
                                        ) xmlt
                        );

    RETURN L_cursor;

END GET_OBJECTS_BY_OWNER;


END WS_REF_CURSOR_SQL;

/