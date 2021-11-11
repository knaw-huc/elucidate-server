--DROP FUNCTION public.annotation_search_by_overlap(character varying, character varying, integer, integer);

-- annotation-start should be before range-end and, and annotation-end should be after range-start

CREATE FUNCTION public.annotation_search_by_overlap(
        _targetid character varying,
        _selectortype character varying,
        _rangestart integer,
        _rangeend integer)
    RETURNS SETOF public.annotation_get AS
$BODY$
    BEGIN
        RETURN QUERY
            SELECT DISTINCT
                a.annotationid,
                a.cachekey,
                ac.collectionid,
                a.createddatetime,
                a.deleted,
                a.json,
                a.modifieddatetime,
                a.id,
                ao.user_id as ownerid,
                agm.group_ids
            FROM
                annotation AS a
                    LEFT JOIN annotation_collection AS ac ON a.collectionid = ac.id
                    LEFT JOIN annotation_target AS at ON at.annotationid = a.id
                    LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                    LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
            WHERE
                at.targetiri = _targetid
                OR at.sourceiri = _targetid
                AND JSONB_PATH_EXISTS(
                        at.json,
                        format(
                            '$."http://www.w3.org/ns/oa#hasSelector"[*] ? (@."@type" == "%s" && @."http://www.w3.org/ns/oa#start"[0]."@value" < %s && @."http://www.w3.org/ns/oa#end"[0]."@value" > %s)',
                            _selectortype,
                            _rangeend,
                            _rangestart
                        )::jsonpath
                )
                AND a.deleted = false
                AND at.deleted = false;
    END;
$BODY$
LANGUAGE plpgsql VOLATILE COST 100 ROWS 1000;

ALTER            FUNCTION public.annotation_search_by_overlap(character varying, character varying, integer, integer) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION public.annotation_search_by_overlap(character varying, character varying, integer, integer) TO postgres;
GRANT EXECUTE ON FUNCTION public.annotation_search_by_overlap(character varying, character varying, integer, integer) TO annotations_role;
REVOKE ALL    ON FUNCTION public.annotation_search_by_overlap(character varying, character varying, integer, integer) FROM public;

-- usage example:
-- select * from public.annotation_search_by_overlap('https://demorepo.tt.di.huc.knaw.nl/task/find/volume-1728/contents', 'urn:example:republic:TextAnchorSelector', 55900, 55910);
--