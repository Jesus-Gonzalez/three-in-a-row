--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.6
-- Dumped by pg_dump version 9.4.6
-- Started on 2016-02-18 20:41:18 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 8 (class 2615 OID 32968)
-- Name: tresenraya; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA tresenraya;


SET search_path = tresenraya, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 177 (class 1259 OID 32985)
-- Name: activaciones; Type: TABLE; Schema: tresenraya; Owner: -
--

CREATE TABLE activaciones (
    aid integer NOT NULL,
    uid bigint NOT NULL,
    clave character varying NOT NULL,
    fecha_limite timestamp without time zone NOT NULL,
    avisado boolean DEFAULT false NOT NULL
);


--
-- TOC entry 176 (class 1259 OID 32983)
-- Name: activaciones_aid_seq; Type: SEQUENCE; Schema: tresenraya; Owner: -
--

CREATE SEQUENCE activaciones_aid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2049 (class 0 OID 0)
-- Dependencies: 176
-- Name: activaciones_aid_seq; Type: SEQUENCE OWNED BY; Schema: tresenraya; Owner: -
--

ALTER SEQUENCE activaciones_aid_seq OWNED BY activaciones.aid;


--
-- TOC entry 181 (class 1259 OID 41571)
-- Name: partidas; Type: TABLE; Schema: tresenraya; Owner: -
--

CREATE TABLE partidas (
    pid character varying NOT NULL,
    desafiante integer NOT NULL,
    desafiado integer NOT NULL,
    aceptado smallint DEFAULT 0 NOT NULL
);


--
-- TOC entry 178 (class 1259 OID 41318)
-- Name: recordarme; Type: TABLE; Schema: tresenraya; Owner: -
--

CREATE TABLE recordarme (
    id character varying NOT NULL,
    token character varying NOT NULL,
    salt character varying NOT NULL,
    uid bigint NOT NULL
);


--
-- TOC entry 175 (class 1259 OID 32971)
-- Name: usuarios; Type: TABLE; Schema: tresenraya; Owner: -
--

CREATE TABLE usuarios (
    uid integer NOT NULL,
    nombre character varying(30) NOT NULL,
    contrasena character varying(50) NOT NULL,
    correo character varying NOT NULL,
    activado boolean NOT NULL,
    pais character(2) NOT NULL,
    fecha_registro timestamp without time zone DEFAULT now() NOT NULL,
    info_partidas integer[] DEFAULT '{0,0,0,0}'::integer[] NOT NULL,
    fecha_conexion timestamp without time zone,
    direccion_ip inet
);


--
-- TOC entry 180 (class 1259 OID 41561)
-- Name: usuarios_conectados; Type: TABLE; Schema: tresenraya; Owner: -
--

CREATE TABLE usuarios_conectados (
    uid bigint NOT NULL,
    nombre character(30) NOT NULL,
    pais character(2)
);


--
-- TOC entry 179 (class 1259 OID 41441)
-- Name: usuarios_registrados; Type: VIEW; Schema: tresenraya; Owner: -
--

CREATE VIEW usuarios_registrados AS
 SELECT usuarios.uid,
    usuarios.nombre
   FROM usuarios
  WHERE (usuarios.activado = true);


--
-- TOC entry 174 (class 1259 OID 32969)
-- Name: usuarios_uid_seq; Type: SEQUENCE; Schema: tresenraya; Owner: -
--

CREATE SEQUENCE usuarios_uid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2050 (class 0 OID 0)
-- Dependencies: 174
-- Name: usuarios_uid_seq; Type: SEQUENCE OWNED BY; Schema: tresenraya; Owner: -
--

ALTER SEQUENCE usuarios_uid_seq OWNED BY usuarios.uid;


--
-- TOC entry 1915 (class 2604 OID 32988)
-- Name: aid; Type: DEFAULT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY activaciones ALTER COLUMN aid SET DEFAULT nextval('activaciones_aid_seq'::regclass);


--
-- TOC entry 1912 (class 2604 OID 32974)
-- Name: uid; Type: DEFAULT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY usuarios ALTER COLUMN uid SET DEFAULT nextval('usuarios_uid_seq'::regclass);


-- Completed on 2016-02-18 20:41:18 CET

--
-- PostgreSQL database dump complete
--

