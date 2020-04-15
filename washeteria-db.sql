--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2
-- Dumped by pg_dump version 12.2

-- Started on 2020-04-16 01:22:52 IST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 8 (class 2615 OID 16385)
-- Name: iiith; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA iiith;


ALTER SCHEMA iiith OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 207 (class 1259 OID 16544)
-- Name: event; Type: TABLE; Schema: iiith; Owner: postgres
--

CREATE TABLE iiith.event (
    eventid bigint NOT NULL,
    starttime timestamp with time zone,
    endtime timestamp with time zone,
    locationid bigint,
    machineid bigint,
    userid character varying,
    modifiedtime timestamp with time zone,
    iscancelled boolean
);


ALTER TABLE iiith.event OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16542)
-- Name: event_eventid_seq; Type: SEQUENCE; Schema: iiith; Owner: postgres
--

CREATE SEQUENCE iiith.event_eventid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE iiith.event_eventid_seq OWNER TO postgres;

--
-- TOC entry 2975 (class 0 OID 0)
-- Dependencies: 206
-- Name: event_eventid_seq; Type: SEQUENCE OWNED BY; Schema: iiith; Owner: postgres
--

ALTER SEQUENCE iiith.event_eventid_seq OWNED BY iiith.event.eventid;


--
-- TOC entry 209 (class 1259 OID 16570)
-- Name: location; Type: TABLE; Schema: iiith; Owner: postgres
--

CREATE TABLE iiith.location (
    locationid bigint NOT NULL,
    name character varying
);


ALTER TABLE iiith.location OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16568)
-- Name: location_locationid_seq; Type: SEQUENCE; Schema: iiith; Owner: postgres
--

CREATE SEQUENCE iiith.location_locationid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE iiith.location_locationid_seq OWNER TO postgres;

--
-- TOC entry 2976 (class 0 OID 0)
-- Dependencies: 208
-- Name: location_locationid_seq; Type: SEQUENCE OWNED BY; Schema: iiith; Owner: postgres
--

ALTER SEQUENCE iiith.location_locationid_seq OWNED BY iiith.location.locationid;


--
-- TOC entry 211 (class 1259 OID 16581)
-- Name: machine; Type: TABLE; Schema: iiith; Owner: postgres
--

CREATE TABLE iiith.machine (
    machineid bigint NOT NULL,
    machinename character varying,
    locationid bigint,
    status character varying,
    availableat timestamp with time zone
);


ALTER TABLE iiith.machine OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16579)
-- Name: machine_machineid_seq; Type: SEQUENCE; Schema: iiith; Owner: postgres
--

CREATE SEQUENCE iiith.machine_machineid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE iiith.machine_machineid_seq OWNER TO postgres;

--
-- TOC entry 2977 (class 0 OID 0)
-- Dependencies: 210
-- Name: machine_machineid_seq; Type: SEQUENCE OWNED BY; Schema: iiith; Owner: postgres
--

ALTER SEQUENCE iiith.machine_machineid_seq OWNED BY iiith.machine.machineid;

--
-- TOC entry 2828 (class 2604 OID 16547)
-- Name: event eventid; Type: DEFAULT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.event ALTER COLUMN eventid SET DEFAULT nextval('iiith.event_eventid_seq'::regclass);


--
-- TOC entry 2829 (class 2604 OID 16573)
-- Name: location locationid; Type: DEFAULT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.location ALTER COLUMN locationid SET DEFAULT nextval('iiith.location_locationid_seq'::regclass);


--
-- TOC entry 2830 (class 2604 OID 16584)
-- Name: machine machineid; Type: DEFAULT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.machine ALTER COLUMN machineid SET DEFAULT nextval('iiith.machine_machineid_seq'::regclass);


--
-- TOC entry 2836 (class 2606 OID 16565)
-- Name: event event_pk; Type: CONSTRAINT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.event
    ADD CONSTRAINT event_pk PRIMARY KEY (eventid);


--
-- TOC entry 2838 (class 2606 OID 16578)
-- Name: location location_pk; Type: CONSTRAINT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.location
    ADD CONSTRAINT location_pk PRIMARY KEY (locationid);


--
-- TOC entry 2840 (class 2606 OID 16589)
-- Name: machine machine_pk; Type: CONSTRAINT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.machine
    ADD CONSTRAINT machine_pk PRIMARY KEY (machineid);

--
-- TOC entry 2841 (class 2606 OID 16595)
-- Name: event event_location_fk; Type: FK CONSTRAINT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.event
    ADD CONSTRAINT event_location_fk FOREIGN KEY (locationid) REFERENCES iiith.location(locationid) NOT VALID;


--
-- TOC entry 2842 (class 2606 OID 16600)
-- Name: event event_machine_fk; Type: FK CONSTRAINT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.event
    ADD CONSTRAINT event_machine_fk FOREIGN KEY (machineid) REFERENCES iiith.machine(machineid) NOT VALID;


--
-- TOC entry 2843 (class 2606 OID 16590)
-- Name: machine machine_location_fk; Type: FK CONSTRAINT; Schema: iiith; Owner: postgres
--

ALTER TABLE ONLY iiith.machine
    ADD CONSTRAINT machine_location_fk FOREIGN KEY (locationid) REFERENCES iiith.location(locationid) NOT VALID;


-- Completed on 2020-04-16 01:22:52 IST

--
-- PostgreSQL database dump complete
--

