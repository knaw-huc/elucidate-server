--begin;

--first, drop functions and views that are dependent on identifier columns
drop function annotation_body_create;
drop function annotation_body_delete;
drop function annotation_collection_create;
drop function annotation_create(character varying, character varying, jsonb);
drop function annotation_create(character varying, character varying, jsonb, integer);
drop function annotation_creator_create;
drop function annotation_creator_delete;
drop function annotation_css_selector_create;
drop function annotation_css_selector_delete;
drop function annotation_data_position_selector_create;
drop function annotation_data_position_selector_delete;
drop function annotation_delete;
drop function annotation_fragment_selector_create;
drop function annotation_fragment_selector_delete;
drop function annotation_generator_create;
drop function annotation_generator_delete;
drop function annotation_history_create;
drop function annotation_history_delete;
drop function annotation_search_by_body;
drop function annotation_search_by_creator;
drop function annotation_search_by_generator;
drop function annotation_search_by_target;
drop function annotation_search_by_temporal;
drop function annotation_svg_selector_create;
drop function annotation_svg_selector_delete;
drop function annotation_target_create;
drop function annotation_target_delete;
drop function annotation_temporal_create;
drop function annotation_temporal_delete;
drop function annotation_text_position_selector_create;
drop function annotation_text_position_selector_delete;
drop function annotation_text_quote_selector_create;
drop function annotation_text_quote_selector_delete;
drop function annotation_update;
drop function annotation_xpath_selector_create;
drop function annotation_xpath_selector_delete;

drop view annotation_body_get;
drop view annotation_collection_get;
drop view annotation_creator_get;
drop view annotation_css_selector_get;
drop view annotation_data_position_selector_get;
drop view annotation_fragment_selector_get;
drop view annotation_generator_get;
drop view annotation_get;
drop view annotation_history_get;
drop view annotation_svg_selector_get;
drop view annotation_target_get;
drop view annotation_temporal_get;
drop view annotation_text_position_selector_get;
drop view annotation_text_quote_selector_get;
drop view annotation_xpath_selector_get;
drop view security_group_annotations;

--set the identifier column type to text
alter table annotation
    alter column annotationid type text;

alter table annotation_body
    alter column bodyiri type text,
    alter column sourceiri type text;

alter table annotation_target
    alter column targetiri type text,
    alter column sourceiri type text;

alter table annotation_collection
    alter column collectionid type text;

alter table annotation_agent
    alter column agentiri type text;

alter table annotation_selector
    alter column selectoriri type text;

----recreate the dropped views and procedures

--
-- Name: annotation_body_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_body_get AS
 SELECT a.annotationid,
    ac.collectionid,
    ab.bodyiri,
    ab.createddatetime,
    ab.deleted,
    ab.json,
    ab.modifieddatetime,
    ab.sourceiri,
    ab.id
   FROM ((public.annotation_body ab
     LEFT JOIN public.annotation a ON ((ab.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)));


ALTER TABLE public.annotation_body_get OWNER TO postgres;

--
-- Name: annotation_body_create(integer, character varying, character varying, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_body_create(_annotationid integer, _bodyiri character varying, _sourceiri character varying, _json jsonb) RETURNS SETOF public.annotation_body_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_body (
            annotationid,
            bodyiri,
            sourceiri,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _annotationid,
            _bodyiri,
            _sourceiri,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                a.annotationid,
                ac.collectionid,
                ab.bodyiri,
                ab.createddatetime,
                ab.deleted,
                ab.json,
                ab.modifieddatetime,
                ab.sourceiri,
                ab.id
            FROM
                annotation_body ab
                    LEFT JOIN annotation a ON ab.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE ab.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_body_create(_annotationid integer, _bodyiri character varying, _sourceiri character varying, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_body_delete(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_body_delete(_annotationid integer) RETURNS SETOF public.annotation_body_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
RETURN QUERY
    WITH updated_rows AS (
        UPDATE
            annotation_body
        SET
            deleted = true,
            modifieddatetime = now()
        WHERE
            annotationid = _annotationid
            AND deleted = false
        RETURNING
            *
)
SELECT
    a.annotationid,
    ac.collectionid,
    ur.bodyiri,
    ur.createddatetime,
    ur.deleted,
    ur.json,
    ur.modifieddatetime,
    ur.sourceiri,
    ur.id
FROM
    updated_rows ur
        LEFT JOIN annotation a ON  ur.annotationid = a.id
        LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_body_delete(_annotationid integer) OWNER TO postgres;

--
-- Name: annotation_collection_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_collection_get AS
 SELECT ac.cachekey,
    ac.collectionid,
    ac.createddatetime,
    ac.deleted,
    ac.json,
    ac.modifieddatetime,
    ac.id
   FROM public.annotation_collection ac;


ALTER TABLE public.annotation_collection_get OWNER TO postgres;

--
-- Name: annotation_collection_create(character varying, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_collection_create(_collectionid character varying, _json jsonb) RETURNS SETOF public.annotation_collection_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_collection (
            collectionid,
            json,
            createddatetime,
            deleted,
            cachekey
        )
        VALUES (
            _collectionid,
            _json,
            _createddatetime,
            false,
            md5(_createddatetime::text)
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                ac.cachekey,
                ac.collectionid,
                ac.createddatetime,
                ac.deleted,
                ac.json,
                ac.modifieddatetime,
                ac.id
            FROM
                annotation_collection ac
            WHERE
                ac.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_collection_create(_collectionid character varying, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_get AS
 SELECT a.annotationid,
    a.cachekey,
    ac.collectionid,
    a.createddatetime,
    a.deleted,
    a.json,
    a.modifieddatetime,
    a.id,
    ao.user_id AS ownerid,
    agm.group_ids
   FROM (((public.annotation a
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
     LEFT JOIN public.annotation_owner ao ON ((ao.annotation_id = a.id)))
     LEFT JOIN public.annotation_group_memberships agm ON ((agm.annotation_id = a.id)));


ALTER TABLE public.annotation_get OWNER TO postgres;

--
-- Name: annotation_create(character varying, character varying, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_create(_collectionid character varying, _annotationid character varying, _json jsonb) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation (
            annotationid,
            collectionid,
            json,
            createddatetime,
            deleted,
            cachekey
        )
        VALUES (
            _annotationid,
            (
                SELECT
                    id
                FROM
                    annotation_collection
                WHERE
                    collectionid = _collectionid
                    AND deleted = false
                ORDER BY
                    modifieddatetime DESC LIMIT 1
            ),
            _json,
            _createddatetime,
            false,
            md5(_createddatetime::text)
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                a.annotationid,
                a.cachekey,
                ac.collectionid,
                a.createddatetime,
                a.deleted,
                a.json,
                a.modifieddatetime,
                a.id
            FROM
                annotation a
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                a.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_create(_collectionid character varying, _annotationid character varying, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_create(character varying, character varying, jsonb, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_create(_collectionid character varying, _annotationid character varying, _json jsonb, _ownerid integer) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation (
            annotationid,
            collectionid,
            json,
            createddatetime,
            deleted,
            cachekey
        )
        VALUES (
            _annotationid,
            (
                SELECT
                    id
                FROM
                    annotation_collection
                WHERE
                    collectionid = _collectionid
                    AND deleted = false
                ORDER BY
                    modifieddatetime DESC LIMIT 1
            ),
            _json,
            _createddatetime,
            false,
            md5(_createddatetime::text)
        ) RETURNING id INTO _insertedid;
        IF _ownerid IS NOT NULL THEN
          INSERT INTO
            annotation_owner
          (user_id, annotation_id)
            VALUES
          (_ownerid, _insertedid);
        END IF;
        RETURN QUERY
            SELECT
                a.*
            FROM
                annotation_get a
            WHERE
                a.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_create(_collectionid character varying, _annotationid character varying, _json jsonb, _ownerid integer) OWNER TO postgres;


--
-- Name: annotation_creator_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_creator_get AS
 SELECT aa.id,
    a.annotationid,
    ac.collectionid,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    aa.agentiri,
    aa.json,
    aat.type,
    aat.json AS typejson,
    aan.name,
    aan.json AS namejson,
    aa.nickname,
    aae.email,
    aae.json AS emailjson,
    aaes.emailsha1,
    aaes.json AS emailsha1json,
    aah.homepage,
    aah.json AS homepagejson,
    aa.createddatetime,
    aa.modifieddatetime,
    aa.deleted
   FROM (((((((((public.annotation_agent aa
     LEFT JOIN public.annotation_agent_email aae ON ((aae.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_email_sha1 aaes ON ((aaes.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_homepage aah ON ((aah.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_name aan ON ((aan.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_type aat ON ((aat.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_body ab ON ((ab.id = aa.bodyid)))
     LEFT JOIN public.annotation_target at ON ((at.id = aa.targetid)))
     LEFT JOIN public.annotation a ON ((a.id = aa.annotationid)))
     LEFT JOIN public.annotation_collection ac ON ((ac.id = a.collectionid)))
  WHERE ((aa.relationshiptype)::text = 'CREATOR'::text);


ALTER TABLE public.annotation_creator_get OWNER TO postgres;

--
-- Name: annotation_creator_create(integer, integer, integer, character varying, jsonb, character varying[], jsonb[], text[], jsonb[], character varying, text[], jsonb[], text[], jsonb[], text[], jsonb[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_creator_create(_annotationid integer, _bodyid integer, _targetid integer, _creatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) RETURNS SETOF public.annotation_creator_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
        _loopvar text;
        _loopindex integer;
    BEGIN
        INSERT INTO annotation_agent (
            annotationid,
            bodyid,
            targetid,
            agentiri,
            nickname,
            relationshiptype,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _annotationid,
            _bodyid,
            _targetid,
            _creatoriri,
            _nickname,
            'CREATOR',
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _types LOOP
            INSERT INTO annotation_agent_type (
                annotationagentid,
                type,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _typesjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _names LOOP
            INSERT INTO annotation_agent_name (
                annotationagentid,
                name,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _namesjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _emails LOOP
            INSERT INTO annotation_agent_email (
                annotationagentid,
                email,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _emailsjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _emailsha1s LOOP
            INSERT INTO annotation_agent_email_sha1 (
                annotationagentid,
                emailsha1,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _emailsha1sjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _homepages LOOP
            INSERT INTO annotation_agent_homepage (
                annotationagentid,
                homepage,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _homepagesjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        RETURN QUERY
            SELECT
                aa.id,
                a.annotationid,
                ac.collectionid,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                aa.agentiri,
                aa.json,
                aat.type,
                aat.json AS typejson,
                aan.name,
                aan.json AS namejson,
                aa.nickname,
                aae.email,
                aae.json AS emailjson,
                aaes.emailsha1,
                aaes.json AS emailsha1json,
                aah.homepage,
                aah.json AS homepagejson,
                aa.createddatetime,
                aa.modifieddatetime,
                aa.deleted
            FROM
                annotation_agent aa
                    LEFT JOIN annotation_agent_email aae ON aae.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_email_sha1 aaes ON aaes.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_homepage aah ON aah.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_name aan ON aan.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_type aat ON aat.annotationagentid = aa.id
                    LEFT JOIN annotation_body ab ON ab.id = aa.bodyid
                    LEFT JOIN annotation_target at ON at.id = aa.targetid
                    LEFT JOIN annotation a ON a.id = aa.annotationid
                    LEFT JOIN annotation_collection ac ON ac.id = a.collectionid
            WHERE
                aa.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_creator_create(_annotationid integer, _bodyid integer, _targetid integer, _creatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) OWNER TO postgres;

--
-- Name: annotation_creator_delete(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_creator_delete(_annotationid integer, _bodyid integer, _targetid integer) RETURNS SETOF public.annotation_creator_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        CREATE TEMPORARY TABLE deleted_creators ON COMMIT DROP AS
            WITH updated_rows AS (
                UPDATE
                    annotation_agent
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _annotationid IS NOT NULL WHEN true THEN (annotationid = _annotationid) ELSE (true) END
                    AND CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND relationshiptype = 'CREATOR'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                id
            FROM
                updated_rows;
        UPDATE annotation_agent_email SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_creators);
        UPDATE annotation_agent_email_sha1 SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_creators);
        UPDATE annotation_agent_homepage SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_creators);
        UPDATE annotation_agent_name SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_creators);
        UPDATE annotation_agent_type SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_creators);
        RETURN QUERY
            SELECT
                aa.id,
                a.annotationid,
                ac.collectionid,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                aa.agentiri,
                aa.json,
                aat.type,
                aat.json AS typejson,
                aan.name,
                aan.json AS namejson,
                aa.nickname,
                aae.email,
                aae.json AS emailjson,
                aaes.emailsha1,
                aaes.json AS emailsha1json,
                aah.homepage,
                aah.json AS homepagejson,
                aa.createddatetime,
                aa.modifieddatetime,
                aa.deleted
            FROM
                annotation_agent aa
                    LEFT JOIN annotation_agent_email aae ON aae.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_email_sha1 aaes ON aaes.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_homepage aah ON aah.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_name aan ON aan.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_type aat ON aat.annotationagentid = aa.id
                    LEFT JOIN annotation_body ab ON ab.id = aa.bodyid
                    LEFT JOIN annotation_target at ON at.id = aa.targetid
                    LEFT JOIN annotation a ON a.id = aa.annotationid
                    LEFT JOIN annotation_collection ac ON ac.id = a.collectionid
            WHERE
                aa.id IN (SELECT id FROM deleted_creators);
    END;
$$;


ALTER FUNCTION public.annotation_creator_delete(_annotationid integer, _bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_css_selector_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_css_selector_get AS
 SELECT asl.id,
    asl.selectoriri,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    a.annotationid,
    ac.collectionid,
    asl.value,
    asl.createddatetime,
    asl.modifieddatetime,
    asl.deleted,
    asl.json
   FROM ((((public.annotation_selector asl
     LEFT JOIN public.annotation_body ab ON ((asl.bodyid = ab.id)))
     LEFT JOIN public.annotation_target at ON ((asl.targetid = at.id)))
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
  WHERE ((asl.type)::text = 'http://www.w3.org/ns/oa#CssSelector'::text);


ALTER TABLE public.annotation_css_selector_get OWNER TO postgres;

--
-- Name: annotation_css_selector_create(integer, integer, character varying, text, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_css_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) RETURNS SETOF public.annotation_css_selector_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid INTEGER;
        _createddatetime TIMESTAMP without TIME zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_selector (
            bodyid,
            targetid,
            selectoriri,
            type,
            value,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _bodyid,
            _targetid,
            _selectoriri,
            'http://www.w3.org/ns/oa#CssSelector',
            _value,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                asl.id,
                asl.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                asl.value,
                asl.createddatetime,
                asl.modifieddatetime,
                asl.deleted,
                asl.json
            FROM
                annotation_selector asl
                    LEFT JOIN annotation_body ab ON asl.bodyid = ab.id
                    LEFT JOIN annotation_target at ON asl.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                asl.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_css_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_css_selector_delete(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_css_selector_delete(_bodyid integer, _targetid integer) RETURNS SETOF public.annotation_css_selector_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (_bodyid IS NULL AND _targetid IS NULL) THEN
            RAISE EXCEPTION 'Both _bodyid AND _targetid cannot be NULL';
        END IF;
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_selector
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND type = 'http://www.w3.org/ns/oa#CssSelector'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                ur.id,
                ur.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                ur.value,
                ur.createddatetime,
                ur.modifieddatetime,
                ur.deleted,
                ur.json
            FROM
                updated_rows ur
                    LEFT JOIN annotation_body ab ON ur.bodyid = ab.id
                    LEFT JOIN annotation_target at ON ur.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_css_selector_delete(_bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_data_position_selector_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_data_position_selector_get AS
 SELECT asl.id,
    asl.selectoriri,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    a.annotationid,
    ac.collectionid,
    asl.start,
    asl."end",
    asl.createddatetime,
    asl.modifieddatetime,
    asl.deleted,
    asl.json
   FROM ((((public.annotation_selector asl
     LEFT JOIN public.annotation_body ab ON ((asl.bodyid = ab.id)))
     LEFT JOIN public.annotation_target at ON ((asl.targetid = at.id)))
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
  WHERE ((asl.type)::text = 'http://www.w3.org/ns/oa#DataPositionSelector'::text);


ALTER TABLE public.annotation_data_position_selector_get OWNER TO postgres;

--
-- Name: annotation_data_position_selector_create(integer, integer, character varying, integer, integer, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_data_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) RETURNS SETOF public.annotation_data_position_selector_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_selector (
            bodyid,
            targetid,
            selectoriri,
            type,
            start,
            "end",
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _bodyid,
            _targetid,
            _selectoriri,
            'http://www.w3.org/ns/oa#DataPositionSelector',
            _start,
            _end,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                asl.id,
                asl.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                asl.start,
                asl."end",
                asl.createddatetime,
                asl.modifieddatetime,
                asl.deleted,
                asl.json
            FROM
                annotation_selector asl
                    LEFT JOIN annotation_body ab ON asl.bodyid = ab.id
                    LEFT JOIN annotation_target at ON asl.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                asl.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_data_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_data_position_selector_delete(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_data_position_selector_delete(_bodyid integer, _targetid integer) RETURNS SETOF public.annotation_data_position_selector_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (_bodyid IS NULL AND _targetid IS NULL) THEN
            RAISE EXCEPTION 'Both _bodyid AND _targetid cannot be NULL';
        END IF;
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_selector
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND type = 'http://www.w3.org/ns/oa#DataPositionSelector'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                ur.id,
                ur.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                ur.start,
                ur.end,
                ur.createddatetime,
                ur.modifieddatetime,
                ur.deleted,
                ur.json
            FROM
                updated_rows ur
                    LEFT JOIN annotation_body ab ON ur.bodyid = ab.id
                    LEFT JOIN annotation_target at ON ur.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_data_position_selector_delete(_bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_delete(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_delete(_collectionid character varying, _annotationid character varying) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        UPDATE
            annotation
        SET
            deleted = true,
            modifieddatetime = now()
        WHERE
            collectionid = (
                SELECT
                    id
                FROM
                    annotation_collection
                WHERE
                    collectionid = _collectionid
                    AND deleted = false
                ORDER BY
                    modifieddatetime DESC LIMIT 1
            )
            AND annotationid = _annotationid
            AND deleted = false;
        RETURN QUERY
            SELECT
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
                annotation a
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
                    LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                    LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
            WHERE
                ac.collectionid = _collectionid
                AND ac.deleted = false
                AND a.annotationid = _annotationid
                AND a.deleted = true
            ORDER BY
                modifieddatetime DESC LIMIT 1;
    END;
$$;


ALTER FUNCTION public.annotation_delete(_collectionid character varying, _annotationid character varying) OWNER TO postgres;

--
-- Name: annotation_fragment_selector_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_fragment_selector_get AS
 SELECT asl.id,
    asl.selectoriri,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    a.annotationid,
    ac.collectionid,
    asl.conformsto,
    asl.value,
    asl.x,
    asl.y,
    asl.w,
    asl.h,
    asl.start,
    asl."end",
    asl.createddatetime,
    asl.modifieddatetime,
    asl.deleted,
    asl.json
   FROM ((((public.annotation_selector asl
     LEFT JOIN public.annotation_body ab ON ((asl.bodyid = ab.id)))
     LEFT JOIN public.annotation_target at ON ((asl.targetid = at.id)))
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
  WHERE ((asl.type)::text = 'http://www.w3.org/ns/oa#FragmentSelector'::text);


ALTER TABLE public.annotation_fragment_selector_get OWNER TO postgres;

--
-- Name: annotation_fragment_selector_create(integer, integer, character varying, text, text, integer, integer, integer, integer, integer, integer, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_fragment_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _conformsto text, _value text, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _json jsonb) RETURNS SETOF public.annotation_fragment_selector_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_selector (
            bodyid,
            targetid,
            selectoriri,
            type,
            conformsto,
            value,
            x,
            y,
            w,
            h,
            start,
            "end",
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _bodyid,
            _targetid,
            _selectoriri,
            'http://www.w3.org/ns/oa#FragmentSelector',
            _conformsto,
            _value,
            _x,
            _y,
            _w,
            _h,
            _start,
            _end,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                asl.id,
                asl.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                asl.conformsto,
                asl.value,
                asl.x,
                asl.y,
                asl.w,
                asl.h,
                asl.start,
                asl.end,
                asl.createddatetime,
                asl.modifieddatetime,
                asl.deleted,
                asl.json
            FROM
                annotation_selector asl
                    LEFT JOIN annotation_body ab ON asl.bodyid = ab.id
                    LEFT JOIN annotation_target at ON asl.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                asl.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_fragment_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _conformsto text, _value text, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_fragment_selector_delete(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_fragment_selector_delete(_bodyid integer, _targetid integer) RETURNS SETOF public.annotation_fragment_selector_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (_bodyid IS NULL AND _targetid IS NULL) THEN
            RAISE EXCEPTION 'Both _bodyid AND _targetid cannot be NULL';
        END IF;
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_selector
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND type = 'http://www.w3.org/ns/oa#FragmentSelector'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                ur.id,
                ur.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                ur.conformsto,
                ur.value,
                ur.x,
                ur.y,
                ur.w,
                ur.h,
                ur.start,
                ur.end,
                ur.createddatetime,
                ur.modifieddatetime,
                ur.deleted,
                ur.json
            FROM
                updated_rows ur
                    LEFT JOIN annotation_body ab ON ur.bodyid = ab.id
                    LEFT JOIN annotation_target at ON ur.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_fragment_selector_delete(_bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_generator_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_generator_get AS
 SELECT aa.id,
    a.annotationid,
    ac.collectionid,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    aa.agentiri,
    aa.json,
    aat.type,
    aat.json AS typejson,
    aan.name,
    aan.json AS namejson,
    aa.nickname,
    aae.email,
    aae.json AS emailjson,
    aaes.emailsha1,
    aaes.json AS emailsha1json,
    aah.homepage,
    aah.json AS homepagejson,
    aa.createddatetime,
    aa.modifieddatetime,
    aa.deleted
   FROM (((((((((public.annotation_agent aa
     LEFT JOIN public.annotation_agent_email aae ON ((aae.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_email_sha1 aaes ON ((aaes.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_homepage aah ON ((aah.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_name aan ON ((aan.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_agent_type aat ON ((aat.annotationagentid = aa.id)))
     LEFT JOIN public.annotation_body ab ON ((ab.id = aa.bodyid)))
     LEFT JOIN public.annotation_target at ON ((at.id = aa.targetid)))
     LEFT JOIN public.annotation a ON ((a.id = aa.annotationid)))
     LEFT JOIN public.annotation_collection ac ON ((ac.id = a.collectionid)))
  WHERE ((aa.relationshiptype)::text = 'GENERATOR'::text);


ALTER TABLE public.annotation_generator_get OWNER TO postgres;

--
-- Name: annotation_generator_create(integer, integer, integer, character varying, jsonb, character varying[], jsonb[], text[], jsonb[], character varying, text[], jsonb[], text[], jsonb[], text[], jsonb[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_generator_create(_annotationid integer, _bodyid integer, _targetid integer, _generatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) RETURNS SETOF public.annotation_generator_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
        _loopvar text;
        _loopindex integer;
    BEGIN
        INSERT INTO annotation_agent (
            annotationid,
            bodyid,
            targetid,
            agentiri,
            nickname,
            relationshiptype,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _annotationid,
            _bodyid,
            _targetid,
            _generatoriri,
            _nickname,
            'GENERATOR',
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _types LOOP
            INSERT INTO annotation_agent_type (
                annotationagentid,
                type,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _typesjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _names LOOP
            INSERT INTO annotation_agent_name (
                annotationagentid,
                name,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _namesjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _emails LOOP
            INSERT INTO annotation_agent_email (
                annotationagentid,
                email,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _emailsjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _emailsha1s LOOP
            INSERT INTO annotation_agent_email_sha1 (
                annotationagentid,
                emailsha1,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _emailsha1sjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        _loopindex := 1;
        FOREACH _loopvar IN ARRAY _homepages LOOP
            INSERT INTO annotation_agent_homepage (
                annotationagentid,
                homepage,
                json,
                createddatetime,
                deleted
            )
            VALUES (
                _insertedid,
                _loopvar,
                _homepagesjson[_loopindex],
                _createddatetime,
                false
            );
            _loopindex := _loopindex + 1;
        END LOOP;
        RETURN QUERY
            SELECT
                aa.id,
                a.annotationid,
                ac.collectionid,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                aa.agentiri,
                aa.json,
                aat.type,
                aat.json AS typejson,
                aan.name,
                aan.json AS namejson,
                aa.nickname,
                aae.email,
                aae.json AS emailjson,
                aaes.emailsha1,
                aaes.json AS emailsha1json,
                aah.homepage,
                aah.json AS homepagejson,
                aa.createddatetime,
                aa.modifieddatetime,
                aa.deleted
            FROM
                annotation_agent aa
                    LEFT JOIN annotation_agent_email aae ON aae.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_email_sha1 aaes ON aaes.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_homepage aah ON aah.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_name aan ON aan.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_type aat ON aat.annotationagentid = aa.id
                    LEFT JOIN annotation_body ab ON ab.id = aa.bodyid
                    LEFT JOIN annotation_target at ON at.id = aa.targetid
                    LEFT JOIN annotation a ON a.id = aa.annotationid
                    LEFT JOIN annotation_collection ac ON ac.id = a.collectionid
            WHERE
                aa.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_generator_create(_annotationid integer, _bodyid integer, _targetid integer, _generatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) OWNER TO postgres;

--
-- Name: annotation_generator_delete(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_generator_delete(_annotationid integer, _bodyid integer, _targetid integer) RETURNS SETOF public.annotation_generator_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        CREATE TEMPORARY TABLE deleted_generators ON COMMIT DROP AS
            WITH updated_rows AS (
                UPDATE
                    annotation_agent
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _annotationid IS NOT NULL WHEN true THEN (annotationid = _annotationid) ELSE (true) END
                    AND CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND relationshiptype = 'GENERATOR'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                id
            FROM
                updated_rows;
        UPDATE annotation_agent_email SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_generators);
        UPDATE annotation_agent_email_sha1 SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_generators);
        UPDATE annotation_agent_homepage SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_generators);
        UPDATE annotation_agent_name SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_generators);
        UPDATE annotation_agent_type SET deleted = true, modifieddatetime = now() WHERE annotationagentid IN (SELECT id FROM deleted_generators);
        RETURN QUERY
            SELECT
                aa.id,
                a.annotationid,
                ac.collectionid,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                aa.agentiri,
                aa.json,
                aat.type,
                aat.json AS typejson,
                aan.name,
                aan.json AS namejson,
                aa.nickname,
                aae.email,
                aae.json AS emailjson,
                aaes.emailsha1,
                aaes.json AS emailsha1json,
                aah.homepage,
                aah.json AS homepagejson,
                aa.createddatetime,
                aa.modifieddatetime,
                aa.deleted
            FROM
                annotation_agent aa
                    LEFT JOIN annotation_agent_email aae ON aae.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_email_sha1 aaes ON aaes.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_homepage aah ON aah.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_name aan ON aan.annotationagentid = aa.id
                    LEFT JOIN annotation_agent_type aat ON aat.annotationagentid = aa.id
                    LEFT JOIN annotation_body ab ON ab.id = aa.bodyid
                    LEFT JOIN annotation_target at ON at.id = aa.targetid
                    LEFT JOIN annotation a ON a.id = aa.annotationid
                    LEFT JOIN annotation_collection ac ON ac.id = a.collectionid
            WHERE
                aa.id IN (SELECT id FROM deleted_generators);
    END;
$$;


ALTER FUNCTION public.annotation_generator_delete(_annotationid integer, _bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_history_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_history_get AS
 SELECT ah.id,
    a.annotationid,
    ac.collectionid,
    ah.json,
    ah.version,
    ah.createddatetime,
    ah.modifieddatetime,
    ah.deleted
   FROM ((public.annotation_history ah
     LEFT JOIN public.annotation a ON ((ah.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)));


ALTER TABLE public.annotation_history_get OWNER TO postgres;

--
-- Name: annotation_history_create(integer, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_history_create(_annotationid integer, _json jsonb) RETURNS SETOF public.annotation_history_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_history (
            annotationid,
            json,
            createddatetime,
            version,
            deleted
        )
        VALUES (
            _annotationid,
            _json,
            _createddatetime,
            (
                SELECT
                    COALESCE(MAX(version), 0) + 1
                FROM
                    annotation_history
                WHERE
                    annotationid = _annotationid
            ),
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                ah.id,
                a.annotationid,
                ac.collectionid,
                ah.json,
                ah.version,
                ah.createddatetime,
                ah.modifieddatetime,
                ah.deleted
            FROM
                annotation_history ah
                    LEFT JOIN annotation a ON ah.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                ah.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_history_create(_annotationid integer, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_history_delete(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_history_delete(_annotationid integer) RETURNS SETOF public.annotation_history_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
    CREATE TEMPORARY TABLE deleted_histories ON COMMIT DROP AS
        WITH updated_rows AS (
            UPDATE
                annotation_history
            SET
                deleted = true,
                modifieddatetime = now()
            WHERE
                annotationid = _annotationid
                AND deleted = false
            RETURNING
                *
        )
        SELECT
            id
        FROM
            updated_rows;
    RETURN QUERY
        SELECT
            ah.id,
            a.annotationid,
            ac.collectionid,
            ah.json,
            ah.version,
            ah.createddatetime,
            ah.modifieddatetime,
            ah.deleted
        FROM
            annotation_history ah
                LEFT JOIN annotation a ON ah.annotationid = a.id
                LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
        WHERE
            ah.id IN (SELECT id FROM deleted_histories);
    END;
$$;


ALTER FUNCTION public.annotation_history_delete(_annotationid integer) OWNER TO postgres;

--
-- Name: annotation_search_by_body(boolean, boolean, character varying, boolean, integer, integer, integer, integer, integer, integer, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_search_by_body(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
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
                    LEFT JOIN annotation_body AS ab ON ab.annotationid = a.id
                    LEFT JOIN annotation_selector AS asl ON asl.bodyid = ab.id
                    LEFT JOIN annotation_agent AS agc ON agc.bodyid = ab.id
                    LEFT JOIN annotation_agent AS agg ON agg.bodyid = ab.id
                    LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                    LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
            WHERE
                (CASE _searchids
                    WHEN true THEN
                        (
                            CASE _strict
                                WHEN true THEN
                                    ab.bodyiri = _value
                                ELSE
                                    ab.bodyiri LIKE (_value || '%')
                            END
                        )
                    ELSE
                        false
                END
                OR CASE _searchsources
                    WHEN true THEN
                        (
                            CASE _strict
                                WHEN true THEN
                                    ab.sourceiri = _value
                                ELSE
                                    ab.sourceiri LIKE (_value || '%')
                            END
                        )
                    ELSE
                        false
                END)
                AND CASE ((_start IS NOT NULL AND _end IS NOT NULL) OR (_x IS NOT NULL AND _y IS NOT NULL AND _w IS NOT NULL AND _h IS NOT NULL))
                    WHEN true THEN
                        (
                            ab.id IN (
                                SELECT
                                    bodyid
                                FROM
                                    annotation_selector
                                WHERE
                                    CASE (_x IS NOT NULL AND _y IS NOT NULL AND _w IS NOT NULL AND _h IS NOT NULL)
                                        WHEN true THEN
                                            (
                                                (_x + _w) > x
                                                AND _x < (x + w)
                                                AND (_y + _h) > y
                                                AND _y < (y + h)
                                                AND type = 'http://www.w3.org/ns/oa#FragmentSelector'
                                                AND deleted = false
                                            )
                                        ELSE
                                            (true)
                                    END
                                INTERSECT
                                SELECT
                                    bodyid
                                FROM
                                    annotation_selector
                                WHERE
                                    CASE (_start IS NOT NULL AND _end IS NOT NULL)
                                        WHEN true THEN
                                            (
                                                _start < "end"
                                                AND _end > start
                                                AND type = 'http://www.w3.org/ns/oa#FragmentSelector'
                                                AND deleted = false
                                            )
                                        ELSE
                                            (true)
                                    END
                            )
                        )

                    ELSE
                        true
                END
                AND CASE (_creatoriri IS NOT NULL)
                    WHEN true THEN
                        (agc.agentiri = _creatoriri AND agc.relationshiptype = 'CREATOR')
                    ELSE
                        true
                END
                AND CASE (_generatoriri IS NOT NULL)
                    WHEN true THEN
                        (agg.agentiri = _generatoriri AND agg.relationshiptype = 'GENERATOR')
                    ELSE
                        true
                END
                AND a.deleted = false
                AND ab.deleted = false;
    END;
$$;


ALTER FUNCTION public.annotation_search_by_body(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) OWNER TO postgres;

--
-- Name: annotation_search_by_creator(boolean, boolean, boolean, character varying, character varying, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_search_by_creator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
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
                    LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                    LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
            WHERE
                a.id IN (
                    SELECT
                        a.id
                    FROM
                        annotation AS a
                            LEFT JOIN annotation_collection AS ac ON a.collectionid = ac.id
                            LEFT JOIN annotation_agent AS aa ON aa.annotationid = a.id
                            LEFT JOIN annotation_agent_email AS aae ON aae.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_email_sha1 AS aaes ON aaes.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_name AS aan ON aan.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_type AS aat ON aat.annotationagentid = aa.id
                    WHERE
                        a.deleted = false
                        AND aa.relationshiptype = 'CREATOR'
                        AND CASE _searchannotations
                            WHEN true THEN
                                CASE
                                    WHEN _type = 'id' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.agentiri = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.agentiri LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'name' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aan.name = _value
                                                    AND aan.deleted = false
                                                ELSE
                                                    aan.name LIKE (_value || '%')
                                                    AND aan.deleted = false
                                            END
                                        )
                                    WHEN _type = 'nickname' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.nickname = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.nickname LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'email' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aae.email = _value
                                                    AND aae.deleted = false
                                                ELSE
                                                    aae.email LIKE (_value || '%')
                                                    AND aae.deleted = false
                                            END
                                        )
                                    WHEN _type = 'emailsha1' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aaes.emailsha1 = _value
                                                    AND aaes.deleted = false
                                                ELSE
                                                    aaes.emailsha1 LIKE (_value || '%')
                                                    AND aaes.deleted = false
                                            END
                                        )
                                END
                            ELSE
                                (false)
                            END
                    UNION
                    SELECT
                        a.id
                    FROM
                        annotation AS a
                            LEFT JOIN annotation_body AS ab ON ab.annotationid = a.id
                            LEFT JOIN annotation_agent AS aa ON aa.bodyid = ab.id
                            LEFT JOIN annotation_agent_email AS aae ON aae.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_email_sha1 AS aaes ON aaes.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_name AS aan ON aan.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_type AS aat ON aat.annotationagentid = aa.id
                    WHERE
                        a.deleted = false
                        AND aa.relationshiptype = 'CREATOR'
                        AND CASE _searchbodies
                            WHEN true THEN
                                CASE
                                    WHEN _type = 'id' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.agentiri = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.agentiri LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'name' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aan.name = _value
                                                    AND aan.deleted = false
                                                ELSE
                                                    aan.name LIKE (_value || '%')
                                                    AND aan.deleted = false
                                            END
                                        )
                                    WHEN _type = 'nickname' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.nickname = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.nickname LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'email' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aae.email = _value
                                                    AND aae.deleted = false
                                                ELSE
                                                    aae.email LIKE (_value || '%')
                                                    AND aae.deleted = false
                                            END
                                        )
                                    WHEN _type = 'emailsha1' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aaes.emailsha1 = _value
                                                    AND aaes.deleted = false
                                                ELSE
                                                    aaes.emailsha1 LIKE (_value || '%')
                                                    AND aaes.deleted = false
                                            END
                                        )
                                END
                            ELSE
                                (false)
                            END
                    UNION
                    SELECT
                        a.id
                    FROM
                        annotation AS a
                            LEFT JOIN annotation_target AS at ON at.annotationid = a.id
                            LEFT JOIN annotation_agent AS aa ON aa.targetid = at.id
                            LEFT JOIN annotation_agent_email AS aae ON aae.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_email_sha1 AS aaes ON aaes.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_name AS aan ON aan.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_type AS aat ON aat.annotationagentid = aa.id
                    WHERE
                        a.deleted = false
                        AND aa.relationshiptype = 'CREATOR'
                        AND CASE _searchtargets
                            WHEN true THEN
                                CASE
                                    WHEN _type = 'id' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.agentiri = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.agentiri LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'name' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aan.name = _value
                                                    AND aan.deleted = false
                                                ELSE
                                                    aan.name LIKE (_value || '%')
                                                    AND aan.deleted = false
                                            END
                                        )
                                    WHEN _type = 'nickname' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.nickname = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.nickname LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'email' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aae.email = _value
                                                    AND aae.deleted = false
                                                ELSE
                                                    aae.email LIKE (_value || '%')
                                                    AND aae.deleted = false
                                            END
                                        )
                                    WHEN _type = 'emailsha1' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aaes.emailsha1 = _value
                                                    AND aaes.deleted = false
                                                ELSE
                                                    aaes.emailsha1 LIKE (_value || '%')
                                                    AND aaes.deleted = false
                                            END
                                        )
                                END
                            ELSE
                                (false)
                            END
                );
END;
$$;


ALTER FUNCTION public.annotation_search_by_creator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) OWNER TO postgres;

--
-- Name: annotation_search_by_generator(boolean, boolean, boolean, character varying, character varying, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_search_by_generator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
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
                    LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                    LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
            WHERE
                a.id IN (
                    SELECT
                        a.id
                    FROM
                        annotation AS a
                            LEFT JOIN annotation_collection AS ac ON a.collectionid = ac.id
                            LEFT JOIN annotation_agent AS aa ON aa.annotationid = a.id
                            LEFT JOIN annotation_agent_email AS aae ON aae.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_email_sha1 AS aaes ON aaes.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_name AS aan ON aan.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_type AS aat ON aat.annotationagentid = aa.id
                    WHERE
                        a.deleted = false
                        AND aa.relationshiptype = 'GENERATOR'
                        AND CASE _searchannotations
                            WHEN true THEN
                                CASE
                                    WHEN _type = 'id' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.agentiri = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.agentiri LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'name' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aan.name = _value
                                                    AND aan.deleted = false
                                                ELSE
                                                    aan.name LIKE (_value || '%')
                                                    AND aan.deleted = false
                                            END
                                        )
                                    WHEN _type = 'nickname' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.nickname = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.nickname LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'email' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aae.email = _value
                                                    AND aae.deleted = false
                                                ELSE
                                                    aae.email LIKE (_value || '%')
                                                    AND aae.deleted = false
                                            END
                                        )
                                    WHEN _type = 'emailsha1' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aaes.emailsha1 = _value
                                                    AND aaes.deleted = false
                                                ELSE
                                                    aaes.emailsha1 LIKE (_value || '%')
                                                    AND aaes.deleted = false
                                            END
                                        )
                                END
                            ELSE
                                (false)
                            END
                    UNION
                    SELECT
                        a.id
                    FROM
                        annotation AS a
                            LEFT JOIN annotation_body AS ab ON ab.annotationid = a.id
                            LEFT JOIN annotation_agent AS aa ON aa.bodyid = ab.id
                            LEFT JOIN annotation_agent_email AS aae ON aae.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_email_sha1 AS aaes ON aaes.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_name AS aan ON aan.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_type AS aat ON aat.annotationagentid = aa.id
                    WHERE
                        a.deleted = false
                        AND aa.relationshiptype = 'GENERATOR'
                        AND CASE _searchbodies
                            WHEN true THEN
                                CASE
                                    WHEN _type = 'id' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.agentiri = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.agentiri LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'name' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aan.name = _value
                                                    AND aan.deleted = false
                                                ELSE
                                                    aan.name LIKE (_value || '%')
                                                    AND aan.deleted = false
                                            END
                                        )
                                    WHEN _type = 'nickname' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.nickname = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.nickname LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'email' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aae.email = _value
                                                    AND aae.deleted = false
                                                ELSE
                                                    aae.email LIKE (_value || '%')
                                                    AND aae.deleted = false
                                            END
                                        )
                                    WHEN _type = 'emailsha1' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aaes.emailsha1 = _value
                                                    AND aaes.deleted = false
                                                ELSE
                                                    aaes.emailsha1 LIKE (_value || '%')
                                                    AND aaes.deleted = false
                                            END
                                        )
                                END
                            ELSE
                                (false)
                            END
                    UNION
                    SELECT
                        a.id
                    FROM
                        annotation AS a
                            LEFT JOIN annotation_target AS at ON at.annotationid = a.id
                            LEFT JOIN annotation_agent AS aa ON aa.targetid = at.id
                            LEFT JOIN annotation_agent_email AS aae ON aae.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_email_sha1 AS aaes ON aaes.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_name AS aan ON aan.annotationagentid = aa.id
                            LEFT JOIN annotation_agent_type AS aat ON aat.annotationagentid = aa.id
                    WHERE
                        a.deleted = false
                        AND aa.relationshiptype = 'GENERATOR'
                        AND CASE _searchtargets
                            WHEN true THEN
                                CASE
                                    WHEN _type = 'id' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.agentiri = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.agentiri LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'name' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aan.name = _value
                                                    AND aan.deleted = false
                                                ELSE
                                                    aan.name LIKE (_value || '%')
                                                    AND aan.deleted = false
                                            END
                                        )
                                    WHEN _type = 'nickname' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aa.nickname = _value
                                                    AND aa.deleted = false
                                                ELSE
                                                    aa.nickname LIKE (_value || '%')
                                                    AND aa.deleted = false
                                            END
                                        )
                                    WHEN _type = 'email' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aae.email = _value
                                                    AND aae.deleted = false
                                                ELSE
                                                    aae.email LIKE (_value || '%')
                                                    AND aae.deleted = false
                                            END
                                        )
                                    WHEN _type = 'emailsha1' THEN
                                        (
                                            CASE _strict
                                                WHEN true THEN
                                                    aaes.emailsha1 = _value
                                                    AND aaes.deleted = false
                                                ELSE
                                                    aaes.emailsha1 LIKE (_value || '%')
                                                    AND aaes.deleted = false
                                            END
                                        )
                                END
                            ELSE
                                (false)
                            END
                );
END;
$$;


ALTER FUNCTION public.annotation_search_by_generator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) OWNER TO postgres;

--
-- Name: annotation_search_by_target(boolean, boolean, character varying, boolean, integer, integer, integer, integer, integer, integer, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_search_by_target(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
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
                    LEFT JOIN annotation_selector AS asl ON asl.targetid = at.id
                    LEFT JOIN annotation_agent AS agc ON agc.targetid = at.id
                    LEFT JOIN annotation_agent AS agg ON agg.targetid = at.id
                    LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                    LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
            WHERE
                (CASE _searchids
                    WHEN true THEN
                        (
                            CASE _strict
                                WHEN true THEN
                                    at.targetiri = _value
                                ELSE
                                    at.targetiri LIKE (_value || '%')
                            END
                        )
                    ELSE
                        false
                END
                OR CASE _searchsources
                    WHEN true THEN
                        (
                            CASE _strict
                                WHEN true THEN
                                    at.sourceiri = _value
                                ELSE
                                    at.sourceiri LIKE (_value || '%')
                            END
                        )
                    ELSE
                        false
                END)
                AND CASE ((_start IS NOT NULL AND _end IS NOT NULL) OR (_x IS NOT NULL AND _y IS NOT NULL AND _w IS NOT NULL AND _h IS NOT NULL))
                    WHEN true THEN
                        (
                            at.id IN (
                                SELECT
                                    targetid
                                FROM
                                    annotation_selector
                                WHERE
                                    CASE (_x IS NOT NULL AND _y IS NOT NULL AND _w IS NOT NULL AND _h IS NOT NULL)
                                        WHEN true THEN
                                            (
                                                (_x + _w) > x
                                                AND _x < (x + w)
                                                AND (_y + _h) > y
                                                AND _y < (y + h)
                                                AND type = 'http://www.w3.org/ns/oa#FragmentSelector'
                                                AND deleted = false
                                            )
                                        ELSE
                                            (true)
                                    END
                                INTERSECT
                                SELECT
                                    targetid
                                FROM
                                    annotation_selector
                                WHERE
                                    CASE (_start IS NOT NULL AND _end IS NOT NULL)
                                        WHEN true THEN
                                            (
                                                _start < "end"
                                                AND _end > start
                                                AND type = 'http://www.w3.org/ns/oa#FragmentSelector'
                                                AND deleted = false
                                            )
                                        ELSE
                                            (true)
                                    END
                            )
                        )

                    ELSE
                        true
                END
                AND CASE (_creatoriri IS NOT NULL)
                    WHEN true THEN
                        (agc.agentiri = _creatoriri AND agc.relationshiptype = 'CREATOR')
                    ELSE
                        true
                END
                AND CASE (_generatoriri IS NOT NULL)
                    WHEN true THEN
                        (agg.agentiri = _generatoriri AND agg.relationshiptype = 'GENERATOR')
                    ELSE
                        true
                END
                AND a.deleted = false
                AND at.deleted = false;
    END;
$$;


ALTER FUNCTION public.annotation_search_by_target(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) OWNER TO postgres;

--
-- Name: annotation_search_by_temporal(boolean, boolean, boolean, character varying[], timestamp without time zone); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_search_by_temporal(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _types character varying[], _since timestamp without time zone) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
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
                  LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                  LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
          WHERE
              a.id IN (
                  SELECT
                      a.id
                  FROM
                      annotation AS a
                          LEFT JOIN annotation_temporal atpc ON atpc.annotationid = a.id
                          LEFT JOIN annotation_temporal atpm ON atpm.annotationid = a.id
                          LEFT JOIN annotation_temporal atpg on atpg.annotationid = a.id
                  WHERE
                      a.deleted = false
                      AND CASE _searchannotations
                          WHEN true THEN
                              CASE WHEN _types @> ARRAY['CREATED']::character varying[] THEN
                                  atpc.type = 'CREATED' AND atpc.value >= _since
                              ELSE
                                  true
                              END
                              AND CASE WHEN _types @> ARRAY['MODIFIED']::character varying[] THEN
                                  atpm.type = 'MODIFIED' AND atpm.value >= _since
                              ELSE
                                  true
                              END
                              AND CASE WHEN _types @> ARRAY['GENERATED']::character varying[] THEN
                                  atpg.type = 'GENERATED' AND atpg.value >= _since
                              ELSE
                                  true
                              END
                          ELSE
                              false
                          END
                  UNION
                  SELECT
                      a.id
                  FROM
                      annotation AS a
                          LEFT JOIN annotation_body ab ON ab.annotationid = a.id
                          LEFT JOIN annotation_temporal atpc ON atpc.bodyid = ab.id
                          LEFT JOIN annotation_temporal atpm ON atpm.bodyid = ab.id
                          LEFT JOIN annotation_temporal atpg on atpg.bodyid = ab.id
                  WHERE
                      a.deleted = false
                      AND CASE _searchbodies
                          WHEN true THEN
                              CASE WHEN _types @> ARRAY['CREATED']::character varying[] THEN
                                  atpc.type = 'CREATED' AND atpc.value >= _since
                              ELSE
                                  true
                              END
                              AND CASE WHEN _types @> ARRAY['MODIFIED']::character varying[] THEN
                                  atpm.type = 'MODIFIED' AND atpm.value >= _since
                              ELSE
                                  true
                              END
                              AND CASE WHEN _types @> ARRAY['GENERATED']::character varying[] THEN
                                  atpg.type = 'GENERATED' AND atpg.value >= _since
                              ELSE
                                  true
                              END
                          ELSE
                              false
                          END
                  UNION
                  SELECT
                      a.id
                  FROM
                      annotation AS a
                          LEFT JOIN annotation_target at ON at.annotationid = a.id
                          LEFT JOIN annotation_temporal atpc ON atpc.targetid = at.id
                          LEFT JOIN annotation_temporal atpm ON atpm.targetid = at.id
                          LEFT JOIN annotation_temporal atpg on atpg.targetid = at.id
                  WHERE
                      a.deleted = false
                      AND CASE _searchtargets
                          WHEN true THEN
                              CASE WHEN _types @> ARRAY['CREATED']::character varying[] THEN
                                  atpc.type = 'CREATED' AND atpc.value >= _since
                              ELSE
                                  true
                              END
                              AND CASE WHEN _types @> ARRAY['MODIFIED']::character varying[] THEN
                                  atpm.type = 'MODIFIED' AND atpm.value >= _since
                              ELSE
                                  true
                              END
                              AND CASE WHEN _types @> ARRAY['GENERATED']::character varying[] THEN
                                  atpg.type = 'GENERATED' AND atpg.value >= _since
                              ELSE
                                  true
                              END
                          ELSE
                              false
                          END
              );
END;
$$;


ALTER FUNCTION public.annotation_search_by_temporal(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _types character varying[], _since timestamp without time zone) OWNER TO postgres;

--
-- Name: annotation_svg_selector_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_svg_selector_get AS
 SELECT asl.id,
    asl.selectoriri,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    a.annotationid,
    ac.collectionid,
    asl.value,
    asl.createddatetime,
    asl.modifieddatetime,
    asl.deleted,
    asl.json
   FROM ((((public.annotation_selector asl
     LEFT JOIN public.annotation_body ab ON ((asl.bodyid = ab.id)))
     LEFT JOIN public.annotation_target at ON ((asl.targetid = at.id)))
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
  WHERE ((asl.type)::text = 'http://www.w3.org/ns/oa#SvgSelector'::text);


ALTER TABLE public.annotation_svg_selector_get OWNER TO postgres;

--
-- Name: annotation_svg_selector_create(integer, integer, character varying, text, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_svg_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) RETURNS SETOF public.annotation_svg_selector_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_selector (
            bodyid,
            targetid,
            selectoriri,
            type,
            value,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _bodyid,
            _targetid,
            _selectoriri,
            'http://www.w3.org/ns/oa#SvgSelector',
            _value,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
             SELECT
                asl.id,
                asl.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                asl.value,
                asl.createddatetime,
                asl.modifieddatetime,
                asl.deleted,
                asl.json
            FROM
                annotation_selector asl
                    LEFT JOIN annotation_body ab ON asl.bodyid = ab.id
                    LEFT JOIN annotation_target at ON asl.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                asl.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_svg_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_svg_selector_delete(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_svg_selector_delete(_bodyid integer, _targetid integer) RETURNS SETOF public.annotation_svg_selector_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (_bodyid IS NULL AND _targetid IS NULL) THEN
            RAISE EXCEPTION 'Both _bodyid AND _targetid cannot be NULL';
        END IF;
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_selector
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND type = 'http://www.w3.org/ns/oa#SvgSelector'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                ur.id,
                ur.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                ur.value,
                ur.createddatetime,
                ur.modifieddatetime,
                ur.deleted,
                ur.json
            FROM
                updated_rows ur
                    LEFT JOIN annotation_body ab ON ur.bodyid = ab.id
                    LEFT JOIN annotation_target at ON ur.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_svg_selector_delete(_bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_target_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_target_get AS
 SELECT a.annotationid,
    ac.collectionid,
    at.targetiri,
    at.createddatetime,
    at.deleted,
    at.json,
    at.modifieddatetime,
    at.sourceiri,
    at.id
   FROM ((public.annotation_target at
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)));


ALTER TABLE public.annotation_target_get OWNER TO postgres;

--
-- Name: annotation_target_create(integer, character varying, character varying, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_target_create(_annotationid integer, _targetiri character varying, _sourceiri character varying, _json jsonb) RETURNS SETOF public.annotation_target_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_target (
            annotationid,
            targetiri,
            sourceiri,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _annotationid,
            _targetiri,
            _sourceiri,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                a.annotationid,
                ac.collectionid,
                at.targetiri,
                at.createddatetime,
                at.deleted,
                at.json,
                at.modifieddatetime,
                at.sourceiri,
                at.id
            FROM
                annotation_target at
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE at.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_target_create(_annotationid integer, _targetiri character varying, _sourceiri character varying, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_target_delete(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_target_delete(_annotationid integer) RETURNS SETOF public.annotation_target_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_target
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    annotationid = _annotationid
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                a.annotationid,
                ac.collectionid,
                ur.targetiri,
                ur.createddatetime,
                ur.deleted,
                ur.json,
                ur.modifieddatetime,
                ur.sourceiri,
                ur.id
            FROM
                updated_rows ur
                    LEFT JOIN annotation a ON  ur.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_target_delete(_annotationid integer) OWNER TO postgres;


--
-- Name: annotation_temporal_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_temporal_get AS
 SELECT atp.id,
    a.annotationid,
    ac.collectionid,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    atp.type,
    atp.value,
    atp.json,
    atp.createddatetime,
    atp.modifieddatetime,
    atp.deleted
   FROM ((((public.annotation_temporal atp
     LEFT JOIN public.annotation_body ab ON ((ab.id = atp.bodyid)))
     LEFT JOIN public.annotation_target at ON ((at.id = atp.targetid)))
     LEFT JOIN public.annotation a ON ((a.id = atp.annotationid)))
     LEFT JOIN public.annotation_collection ac ON ((ac.id = a.collectionid)));


ALTER TABLE public.annotation_temporal_get OWNER TO postgres;

--
-- Name: annotation_temporal_create(integer, integer, integer, character varying, timestamp without time zone, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_temporal_create(_annotationid integer, _bodyid integer, _targetid integer, _type character varying, _value timestamp without time zone, _json jsonb) RETURNS SETOF public.annotation_temporal_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_temporal (
            annotationid,
            bodyid,
            targetid,
            type,
            value,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _annotationid,
            _bodyid,
            _targetid,
            _type,
            _value,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                atp.id,
                a.annotationid,
                ac.collectionid,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                atp.type,
                atp.value,
                atp.json,
                atp.createddatetime,
                atp.modifieddatetime,
                atp.deleted
            FROM
                annotation_temporal atp
                    LEFT JOIN annotation_body ab ON ab.id = atp.bodyid
                    LEFT JOIN annotation_target at ON at.id = atp.targetid
                    LEFT JOIN annotation a ON a.id = atp.annotationid
                    LEFT JOIN annotation_collection ac ON ac.id = a.collectionid
                WHERE
                    atp.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_temporal_create(_annotationid integer, _bodyid integer, _targetid integer, _type character varying, _value timestamp without time zone, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_temporal_delete(integer, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_temporal_delete(_annotationid integer, _bodyid integer, _targetid integer) RETURNS SETOF public.annotation_temporal_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        CREATE TEMPORARY TABLE deleted_temporals ON COMMIT DROP AS
            WITH updated_rows AS (
                UPDATE
                    annotation_temporal
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _annotationid IS NOT NULL WHEN true THEN (annotationid = _annotationid) ELSE (true) END
                    AND CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                id
            FROM
                updated_rows;
        RETURN QUERY
            SELECT
                atp.id,
                a.annotationid,
                ac.collectionid,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                atp.type,
                atp.value,
                atp.json,
                atp.createddatetime,
                atp.modifieddatetime,
                atp.deleted
            FROM
                annotation_temporal atp
                    LEFT JOIN annotation_body ab ON ab.id = atp.bodyid
                    LEFT JOIN annotation_target at ON at.id = atp.targetid
                    LEFT JOIN annotation a ON a.id = atp.annotationid
                    LEFT JOIN annotation_collection ac ON ac.id = a.collectionid
            WHERE
                atp.id IN (SELECT id FROM deleted_temporals);
    END;
$$;


ALTER FUNCTION public.annotation_temporal_delete(_annotationid integer, _bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_text_position_selector_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_text_position_selector_get AS
 SELECT asl.id,
    asl.selectoriri,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    a.annotationid,
    ac.collectionid,
    asl.start,
    asl."end",
    asl.createddatetime,
    asl.modifieddatetime,
    asl.deleted,
    asl.json
   FROM ((((public.annotation_selector asl
     LEFT JOIN public.annotation_body ab ON ((asl.bodyid = ab.id)))
     LEFT JOIN public.annotation_target at ON ((asl.targetid = at.id)))
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
  WHERE ((asl.type)::text = 'http://www.w3.org/ns/oa#TextPositionSelector'::text);


ALTER TABLE public.annotation_text_position_selector_get OWNER TO postgres;

--
-- Name: annotation_text_position_selector_create(integer, integer, character varying, integer, integer, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_text_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) RETURNS SETOF public.annotation_text_position_selector_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_selector (
            bodyid,
            targetid,
            selectoriri,
            type,
            start,
            "end",
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _bodyid,
            _targetid,
            _selectoriri,
            'http://www.w3.org/ns/oa#TextPositionSelector',
            _start,
            _end,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                asl.id,
                asl.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                asl.start,
                asl."end",
                asl.createddatetime,
                asl.modifieddatetime,
                asl.deleted,
                asl.json
            FROM
                annotation_selector asl
                    LEFT JOIN annotation_body ab ON asl.bodyid = ab.id
                    LEFT JOIN annotation_target at ON asl.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                asl.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_text_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_text_position_selector_delete(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_text_position_selector_delete(_bodyid integer, _targetid integer) RETURNS SETOF public.annotation_text_position_selector_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (_bodyid IS NULL AND _targetid IS NULL) THEN
            RAISE EXCEPTION 'Both _bodyid AND _targetid cannot be NULL';
        END IF;
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_selector
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND type = 'http://www.w3.org/ns/oa#TextPositionSelector'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                ur.id,
                ur.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                ur.start,
                ur.end,
                ur.createddatetime,
                ur.modifieddatetime,
                ur.deleted,
                ur.json
            FROM
                updated_rows ur
                    LEFT JOIN annotation_body ab ON ur.bodyid = ab.id
                    LEFT JOIN annotation_target at ON ur.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_text_position_selector_delete(_bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_text_quote_selector_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_text_quote_selector_get AS
 SELECT asl.id,
    asl.selectoriri,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    a.annotationid,
    ac.collectionid,
    asl.exact,
    asl.prefix,
    asl.suffix,
    asl.createddatetime,
    asl.modifieddatetime,
    asl.deleted,
    asl.json
   FROM ((((public.annotation_selector asl
     LEFT JOIN public.annotation_body ab ON ((asl.bodyid = ab.id)))
     LEFT JOIN public.annotation_target at ON ((asl.targetid = at.id)))
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
  WHERE ((asl.type)::text = 'http://www.w3.org/ns/oa#TextQuoteSelector'::text);


ALTER TABLE public.annotation_text_quote_selector_get OWNER TO postgres;

--
-- Name: annotation_text_quote_selector_create(integer, integer, character varying, text, text, text, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_text_quote_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _exact text, _prefix text, _suffix text, _json jsonb) RETURNS SETOF public.annotation_text_quote_selector_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_selector (
            bodyid,
            targetid,
            selectoriri,
            type,
            exact,
            prefix,
            suffix,
            json,
            createddatetime,
            deleted)
        VALUES (
            _bodyid,
            _targetid,
            _selectoriri,
            'http://www.w3.org/ns/oa#TextQuoteSelector',
            _exact,
            _prefix,
            _suffix,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                asl.id,
                asl.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                asl.exact,
                asl.prefix,
                asl.suffix,
                asl.createddatetime,
                asl.modifieddatetime,
                asl.deleted,
                asl.json
            FROM annotation_selector asl
                LEFT JOIN annotation_body ab ON asl.bodyid = ab.id
                LEFT JOIN annotation_target at ON asl.targetid = at.id
                LEFT JOIN annotation a ON at.annotationid = a.id
                LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                asl.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_text_quote_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _exact text, _prefix text, _suffix text, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_text_quote_selector_delete(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_text_quote_selector_delete(_bodyid integer, _targetid integer) RETURNS SETOF public.annotation_text_quote_selector_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (_bodyid IS NULL AND _targetid IS NULL) THEN
            RAISE EXCEPTION 'Both _bodyid AND _targetid cannot be NULL';
        END IF;
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_selector
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND type = 'http://www.w3.org/ns/oa#TextQuoteSelector'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                ur.id,
                ur.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                ur.exact,
                ur.prefix,
                ur.suffix,
                ur.createddatetime,
                ur.modifieddatetime,
                ur.deleted,
                ur.json
            FROM
                updated_rows ur
                    LEFT JOIN annotation_body ab ON ur.bodyid = ab.id
                    LEFT JOIN annotation_target at ON ur.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_text_quote_selector_delete(_bodyid integer, _targetid integer) OWNER TO postgres;

--
-- Name: annotation_update(character varying, character varying, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_update(_collectionid character varying, _annotationid character varying, _json jsonb) RETURNS SETOF public.annotation_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _modifieddatetime timestamp without time zone DEFAULT now();
    BEGIN
        UPDATE
            annotation
        SET
            json = _json,
            modifieddatetime = _modifieddatetime,
            cachekey = md5(_modifieddatetime::text)
        WHERE
            collectionid = (
                SELECT
                    id
                FROM
                    annotation_collection
                WHERE
                    collectionid = _collectionid
                    AND deleted = false
                ORDER BY
                    modifieddatetime DESC LIMIT 1
            )
            AND annotationid = _annotationid
            AND deleted = false;
        RETURN QUERY
            SELECT
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
                annotation a
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
                    LEFT JOIN annotation_owner ao ON ao.annotation_id = a.id
                    LEFT JOIN annotation_group_memberships agm ON agm.annotation_id = a.id
            WHERE
                ac.collectionid = _collectionid
                AND ac.deleted = false
                AND a.annotationid = _annotationid
                AND a.deleted = false
            ORDER BY
                modifieddatetime DESC LIMIT 1;
    END;
$$;


ALTER FUNCTION public.annotation_update(_collectionid character varying, _annotationid character varying, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_xpath_selector_get; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.annotation_xpath_selector_get AS
 SELECT asl.id,
    asl.selectoriri,
    ab.bodyiri,
    ab.sourceiri AS bodysourceiri,
    at.targetiri,
    at.sourceiri AS targetsourceiri,
    a.annotationid,
    ac.collectionid,
    asl.value,
    asl.createddatetime,
    asl.modifieddatetime,
    asl.deleted,
    asl.json
   FROM ((((public.annotation_selector asl
     LEFT JOIN public.annotation_body ab ON ((asl.bodyid = ab.id)))
     LEFT JOIN public.annotation_target at ON ((asl.targetid = at.id)))
     LEFT JOIN public.annotation a ON ((at.annotationid = a.id)))
     LEFT JOIN public.annotation_collection ac ON ((a.collectionid = ac.id)))
  WHERE ((asl.type)::text = 'http://www.w3.org/ns/oa#XPathSelector'::text);


ALTER TABLE public.annotation_xpath_selector_get OWNER TO postgres;

--
-- Name: annotation_xpath_selector_create(integer, integer, character varying, text, jsonb); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_xpath_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) RETURNS SETOF public.annotation_xpath_selector_get
    LANGUAGE plpgsql
    AS $$
    DECLARE
        _insertedid integer;
        _createddatetime timestamp without time zone DEFAULT now();
    BEGIN
        INSERT INTO annotation_selector (
            bodyid,
            targetid,
            selectoriri,
            type,
            value,
            json,
            createddatetime,
            deleted
        )
        VALUES (
            _bodyid,
            _targetid,
            _selectoriri,
            'http://www.w3.org/ns/oa#XPathSelector',
            _value,
            _json,
            _createddatetime,
            false
        ) RETURNING id INTO _insertedid;
        RETURN QUERY
            SELECT
                asl.id,
                asl.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                asl.value,
                asl.createddatetime,
                asl.modifieddatetime,
                asl.deleted,
                asl.json
            FROM
                annotation_selector asl
                    LEFT JOIN annotation_body ab ON asl.bodyid = ab.id
                    LEFT JOIN annotation_target at ON asl.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id
            WHERE
                asl.id = _insertedid;
    END;
$$;


ALTER FUNCTION public.annotation_xpath_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) OWNER TO postgres;

--
-- Name: annotation_xpath_selector_delete(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.annotation_xpath_selector_delete(_bodyid integer, _targetid integer) RETURNS SETOF public.annotation_xpath_selector_get
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (_bodyid IS NULL AND _targetid IS NULL) THEN
            RAISE EXCEPTION 'Both _bodyid AND _targetid cannot be NULL';
        END IF;
        RETURN QUERY
            WITH updated_rows AS (
                UPDATE
                    annotation_selector
                SET
                    deleted = true,
                    modifieddatetime = now()
                WHERE
                    CASE _bodyid IS NOT NULL WHEN true THEN (bodyid = _bodyid) ELSE (true) END
                    AND CASE _targetid IS NOT NULL WHEN true THEN (targetid = _targetid) ELSE (true) END
                    AND type = 'http://www.w3.org/ns/oa#XPathSelector'
                    AND deleted = false
                RETURNING
                    *
            )
            SELECT
                ur.id,
                ur.selectoriri,
                ab.bodyiri,
                ab.sourceiri AS bodysourceiri,
                at.targetiri,
                at.sourceiri AS targetsourceiri,
                a.annotationid,
                ac.collectionid,
                ur.value,
                ur.createddatetime,
                ur.modifieddatetime,
                ur.deleted,
                ur.json
            FROM
                updated_rows ur
                    LEFT JOIN annotation_body ab ON ur.bodyid = ab.id
                    LEFT JOIN annotation_target at ON ur.targetid = at.id
                    LEFT JOIN annotation a ON at.annotationid = a.id
                    LEFT JOIN annotation_collection ac ON a.collectionid = ac.id;
    END;
$$;


ALTER FUNCTION public.annotation_xpath_selector_delete(_bodyid integer, _targetid integer) OWNER TO postgres;




--
-- Name: security_group_annotations; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.security_group_annotations AS
 SELECT a.annotationid AS id,
    c.collectionid,
    sg.id AS groupid
   FROM (((public.security_group sg
     JOIN public.annotation_group_membership agm ON ((agm.group_id = sg.id)))
     JOIN public.annotation a ON ((a.id = agm.annotation_id)))
     JOIN public.annotation_collection c ON ((c.id = a.collectionid)));


ALTER TABLE public.security_group_annotations OWNER TO postgres;


--
-- Name: TABLE annotation_body_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_body_get TO annotations_role;


--
-- Name: FUNCTION annotation_body_create(_annotationid integer, _bodyiri character varying, _sourceiri character varying, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_body_create(_annotationid integer, _bodyiri character varying, _sourceiri character varying, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_body_create(_annotationid integer, _bodyiri character varying, _sourceiri character varying, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_body_delete(_annotationid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_body_delete(_annotationid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_body_delete(_annotationid integer) TO annotations_role;


--
-- Name: TABLE annotation_collection_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_collection_get TO annotations_role;


--
-- Name: FUNCTION annotation_collection_create(_collectionid character varying, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_collection_create(_collectionid character varying, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_collection_create(_collectionid character varying, _json jsonb) TO annotations_role;


--
-- Name: TABLE annotation_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_get TO annotations_role;


--
-- Name: FUNCTION annotation_create(_collectionid character varying, _annotationid character varying, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_create(_collectionid character varying, _annotationid character varying, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_create(_collectionid character varying, _annotationid character varying, _json jsonb) TO annotations_role;

--
-- Name: TABLE annotation_creator_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_creator_get TO annotations_role;


--
-- Name: FUNCTION annotation_creator_create(_annotationid integer, _bodyid integer, _targetid integer, _creatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_creator_create(_annotationid integer, _bodyid integer, _targetid integer, _creatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_creator_create(_annotationid integer, _bodyid integer, _targetid integer, _creatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) TO annotations_role;


--
-- Name: FUNCTION annotation_creator_delete(_annotationid integer, _bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_creator_delete(_annotationid integer, _bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_creator_delete(_annotationid integer, _bodyid integer, _targetid integer) TO annotations_role;



--
-- Name: TABLE annotation_css_selector_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_css_selector_get TO annotations_role;


--
-- Name: FUNCTION annotation_css_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_css_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_css_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_css_selector_delete(_bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_css_selector_delete(_bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_css_selector_delete(_bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: TABLE annotation_data_position_selector_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_data_position_selector_get TO annotations_role;


--
-- Name: FUNCTION annotation_data_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_data_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_data_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_data_position_selector_delete(_bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_data_position_selector_delete(_bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_data_position_selector_delete(_bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: FUNCTION annotation_delete(_collectionid character varying, _annotationid character varying); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_delete(_collectionid character varying, _annotationid character varying) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_delete(_collectionid character varying, _annotationid character varying) TO annotations_role;


--
-- Name: TABLE annotation_fragment_selector_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_fragment_selector_get TO annotations_role;


--
-- Name: FUNCTION annotation_fragment_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _conformsto text, _value text, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_fragment_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _conformsto text, _value text, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_fragment_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _conformsto text, _value text, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_fragment_selector_delete(_bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_fragment_selector_delete(_bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_fragment_selector_delete(_bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: TABLE annotation_generator_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_generator_get TO annotations_role;


--
-- Name: FUNCTION annotation_generator_create(_annotationid integer, _bodyid integer, _targetid integer, _generatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_generator_create(_annotationid integer, _bodyid integer, _targetid integer, _generatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_generator_create(_annotationid integer, _bodyid integer, _targetid integer, _generatoriri character varying, _json jsonb, _types character varying[], _typesjson jsonb[], _names text[], _namesjson jsonb[], _nickname character varying, _emails text[], _emailsjson jsonb[], _emailsha1s text[], _emailsha1sjson jsonb[], _homepages text[], _homepagesjson jsonb[]) TO annotations_role;


--
-- Name: FUNCTION annotation_generator_delete(_annotationid integer, _bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_generator_delete(_annotationid integer, _bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_generator_delete(_annotationid integer, _bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: TABLE annotation_history_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_history_get TO annotations_role;


--
-- Name: FUNCTION annotation_history_create(_annotationid integer, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_history_create(_annotationid integer, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_history_create(_annotationid integer, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_history_delete(_annotationid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_history_delete(_annotationid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_history_delete(_annotationid integer) TO annotations_role;


--
-- Name: FUNCTION annotation_search_by_body(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_search_by_body(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_search_by_body(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) TO annotations_role;


--
-- Name: FUNCTION annotation_search_by_creator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_search_by_creator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_search_by_creator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) TO annotations_role;


--
-- Name: FUNCTION annotation_search_by_generator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_search_by_generator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_search_by_generator(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _type character varying, _value character varying, _strict boolean) TO annotations_role;


--
-- Name: FUNCTION annotation_search_by_target(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_search_by_target(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_search_by_target(_searchids boolean, _searchsources boolean, _value character varying, _strict boolean, _x integer, _y integer, _w integer, _h integer, _start integer, _end integer, _creatoriri character varying, _generatoriri character varying) TO annotations_role;


--
-- Name: FUNCTION annotation_search_by_temporal(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _types character varying[], _since timestamp without time zone); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_search_by_temporal(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _types character varying[], _since timestamp without time zone) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_search_by_temporal(_searchannotations boolean, _searchbodies boolean, _searchtargets boolean, _types character varying[], _since timestamp without time zone) TO annotations_role;


--
-- Name: TABLE annotation_svg_selector_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_svg_selector_get TO annotations_role;


--
-- Name: FUNCTION annotation_svg_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_svg_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_svg_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_svg_selector_delete(_bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_svg_selector_delete(_bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_svg_selector_delete(_bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: TABLE annotation_target_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_target_get TO annotations_role;


--
-- Name: FUNCTION annotation_target_create(_annotationid integer, _targetiri character varying, _sourceiri character varying, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_target_create(_annotationid integer, _targetiri character varying, _sourceiri character varying, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_target_create(_annotationid integer, _targetiri character varying, _sourceiri character varying, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_target_delete(_annotationid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_target_delete(_annotationid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_target_delete(_annotationid integer) TO annotations_role;



--
-- Name: TABLE annotation_temporal_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_temporal_get TO annotations_role;


--
-- Name: FUNCTION annotation_temporal_create(_annotationid integer, _bodyid integer, _targetid integer, _type character varying, _value timestamp without time zone, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_temporal_create(_annotationid integer, _bodyid integer, _targetid integer, _type character varying, _value timestamp without time zone, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_temporal_create(_annotationid integer, _bodyid integer, _targetid integer, _type character varying, _value timestamp without time zone, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_temporal_delete(_annotationid integer, _bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_temporal_delete(_annotationid integer, _bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_temporal_delete(_annotationid integer, _bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: TABLE annotation_text_position_selector_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_text_position_selector_get TO annotations_role;


--
-- Name: FUNCTION annotation_text_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_text_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_text_position_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _start integer, _end integer, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_text_position_selector_delete(_bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_text_position_selector_delete(_bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_text_position_selector_delete(_bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: TABLE annotation_text_quote_selector_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_text_quote_selector_get TO annotations_role;


--
-- Name: FUNCTION annotation_text_quote_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _exact text, _prefix text, _suffix text, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_text_quote_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _exact text, _prefix text, _suffix text, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_text_quote_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _exact text, _prefix text, _suffix text, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_text_quote_selector_delete(_bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_text_quote_selector_delete(_bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_text_quote_selector_delete(_bodyid integer, _targetid integer) TO annotations_role;


--
-- Name: FUNCTION annotation_update(_collectionid character varying, _annotationid character varying, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_update(_collectionid character varying, _annotationid character varying, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_update(_collectionid character varying, _annotationid character varying, _json jsonb) TO annotations_role;


--
-- Name: TABLE annotation_xpath_selector_get; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.annotation_xpath_selector_get TO annotations_role;


--
-- Name: FUNCTION annotation_xpath_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_xpath_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_xpath_selector_create(_bodyid integer, _targetid integer, _selectoriri character varying, _value text, _json jsonb) TO annotations_role;


--
-- Name: FUNCTION annotation_xpath_selector_delete(_bodyid integer, _targetid integer); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION public.annotation_xpath_selector_delete(_bodyid integer, _targetid integer) FROM PUBLIC;
GRANT ALL ON FUNCTION public.annotation_xpath_selector_delete(_bodyid integer, _targetid integer) TO annotations_role;

--rollback;
