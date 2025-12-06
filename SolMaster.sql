--
-- PostgreSQL database dump
--

\restrict z664sdCTdDYDpspkMBmmLglsHIrDtOitdXIglW0NdLIMbOtzfaj6b3CyKzpy8Vi

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2025-12-06 03:18:21 -05

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3616 (class 1262 OID 16762)
-- Name: SolMaster; Type: DATABASE; Schema: -; Owner: postgres
--




\unrestrict z664sdCTdDYDpspkMBmmLglsHIrDtOitdXIglW0NdLIMbOtzfaj6b3CyKzpy8Vi
\restrict z664sdCTdDYDpspkMBmmLglsHIrDtOitdXIglW0NdLIMbOtzfaj6b3CyKzpy8Vi

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 244 (class 1259 OID 16965)
-- Name: checkout_pendiente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.checkout_pendiente (
    id bigint NOT NULL,
    preference_id character varying(255),
    cliente_id bigint NOT NULL,
    items_json text NOT NULL,
    direccion character varying(255),
    ciudad character varying(255),
    pais character varying(255),
    codigo_postal character varying(255),
    total numeric(10,2),
    estado character varying(20) NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_expiracion timestamp without time zone
);


ALTER TABLE public.checkout_pendiente OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 16964)
-- Name: checkout_pendiente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.checkout_pendiente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.checkout_pendiente_id_seq OWNER TO postgres;

--
-- TOC entry 3617 (class 0 OID 0)
-- Dependencies: 243
-- Name: checkout_pendiente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.checkout_pendiente_id_seq OWNED BY public.checkout_pendiente.id;


--
-- TOC entry 222 (class 1259 OID 16773)
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    id_cliente bigint NOT NULL,
    id_usuario bigint,
    nom character varying(150),
    apat character varying(100),
    amat character varying(100),
    dni character varying(15),
    cel character varying(20),
    email character varying(150),
    fen date,
    estado boolean DEFAULT true NOT NULL,
    ciudad character varying(255),
    codigo_postal character varying(255),
    direccion character varying(255),
    pais character varying(255)
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16772)
-- Name: cliente_id_cliente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.cliente ALTER COLUMN id_cliente ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.cliente_id_cliente_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 234 (class 1259 OID 16869)
-- Name: compra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.compra (
    id_compra bigint NOT NULL,
    id_proveedor bigint,
    subtotal numeric(12,2) DEFAULT 0 NOT NULL,
    igv numeric(12,2) DEFAULT 0 NOT NULL,
    total numeric(12,2) DEFAULT 0 NOT NULL,
    creado_en timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.compra OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16868)
-- Name: compra_id_compra_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.compra ALTER COLUMN id_compra ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.compra_id_compra_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 236 (class 1259 OID 16885)
-- Name: compra_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.compra_item (
    id_compra_item bigint NOT NULL,
    id_compra bigint NOT NULL,
    id_producto bigint NOT NULL,
    costo_unit numeric(10,2) NOT NULL,
    cantidad integer NOT NULL,
    total_linea numeric(12,2) NOT NULL,
    CONSTRAINT chk_compra_item_cantidad CHECK ((cantidad > 0))
);


ALTER TABLE public.compra_item OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 16884)
-- Name: compra_item_id_compra_item_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.compra_item ALTER COLUMN id_compra_item ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.compra_item_id_compra_item_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 238 (class 1259 OID 16902)
-- Name: comprobante; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comprobante (
    id bigint NOT NULL,
    fecha_emision timestamp(6) without time zone NOT NULL,
    igv numeric(10,2),
    moneda character varying(3) NOT NULL,
    numero character varying(20) NOT NULL,
    ruc_emisor character varying(11) NOT NULL,
    serie character varying(10) NOT NULL,
    subtotal numeric(10,2),
    total numeric(10,2),
    venta_id bigint NOT NULL
);


ALTER TABLE public.comprobante OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 16901)
-- Name: comprobante_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.comprobante ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.comprobante_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 16791)
-- Name: empleado; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.empleado (
    id_empleado bigint NOT NULL,
    id_usuario bigint,
    nom character varying(150),
    apat character varying(100),
    amat character varying(100),
    dni character varying(15),
    cel character varying(20),
    email character varying(150),
    fen date,
    cargo character varying(100),
    sueldo numeric(10,2),
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE public.empleado OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16790)
-- Name: empleado_id_empleado_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.empleado ALTER COLUMN id_empleado ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.empleado_id_empleado_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 230 (class 1259 OID 16834)
-- Name: pedido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pedido (
    id_pedido bigint NOT NULL,
    id_cliente bigint,
    estado character varying(20) NOT NULL,
    subtotal numeric(12,2) DEFAULT 0 NOT NULL,
    igv numeric(12,2) DEFAULT 0 NOT NULL,
    total numeric(12,2) DEFAULT 0 NOT NULL,
    contacto_nombre character varying(150),
    contacto_email character varying(150),
    contacto_cel character varying(30),
    envio_direccion character varying(250),
    pago_tipo character varying(30),
    pago_ref character varying(100),
    comp_tipo character varying(20),
    comp_serie character varying(10),
    comp_numero character varying(20),
    creado_en timestamp without time zone DEFAULT now() NOT NULL,
    pagado_en timestamp without time zone,
    CONSTRAINT chk_pedido_estado CHECK (((estado)::text = ANY (ARRAY[('CARRITO'::character varying)::text, ('PAGADO'::character varying)::text, ('CANCELADO'::character varying)::text])))
);


ALTER TABLE public.pedido OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16833)
-- Name: pedido_id_pedido_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.pedido ALTER COLUMN id_pedido ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.pedido_id_pedido_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 232 (class 1259 OID 16852)
-- Name: pedido_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pedido_item (
    id_pedido_item bigint NOT NULL,
    id_pedido bigint NOT NULL,
    id_producto bigint NOT NULL,
    nombre_prod character varying(150) NOT NULL,
    precio_unit numeric(10,2) NOT NULL,
    cantidad integer NOT NULL,
    total_linea numeric(12,2) NOT NULL,
    CONSTRAINT chk_pedido_item_cantidad CHECK ((cantidad > 0))
);


ALTER TABLE public.pedido_item OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16851)
-- Name: pedido_item_id_pedido_item_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.pedido_item ALTER COLUMN id_pedido_item ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.pedido_item_id_pedido_item_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 16818)
-- Name: producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto (
    id_producto bigint NOT NULL,
    nombre_producto character varying(150) NOT NULL,
    descripcion character varying(500),
    precio numeric(10,2) DEFAULT 0 NOT NULL,
    stock integer DEFAULT 0 NOT NULL,
    marca character varying(100),
    categoria character varying(100),
    id_proveedor bigint,
    imagen_url character varying(500),
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE public.producto OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16817)
-- Name: producto_id_producto_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.producto ALTER COLUMN id_producto ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.producto_id_producto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 16809)
-- Name: proveedor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proveedor (
    id_proveedor bigint NOT NULL,
    razon_social character varying(150),
    ruc character varying(15),
    cel character varying(20),
    email character varying(150),
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE public.proveedor OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16808)
-- Name: proveedor_id_proveedor_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.proveedor ALTER COLUMN id_proveedor ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.proveedor_id_proveedor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 246 (class 1259 OID 17069)
-- Name: solicitud_reembolso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.solicitud_reembolso (
    id bigint NOT NULL,
    comentario_empleado character varying(500),
    estado character varying(20) NOT NULL,
    fecha_procesamiento timestamp(6) without time zone,
    fecha_solicitud timestamp(6) without time zone NOT NULL,
    motivo character varying(500),
    cliente_id bigint NOT NULL,
    procesado_por_id bigint,
    venta_id bigint NOT NULL
);


ALTER TABLE public.solicitud_reembolso OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 17068)
-- Name: solicitud_reembolso_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.solicitud_reembolso ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.solicitud_reembolso_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16764)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id_usuario bigint NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16763)
-- Name: usuario_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.usuario ALTER COLUMN id_usuario ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.usuario_id_usuario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 240 (class 1259 OID 16921)
-- Name: venta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venta (
    id bigint NOT NULL,
    fecha_venta timestamp(6) without time zone NOT NULL,
    total numeric(10,2),
    cliente_id bigint NOT NULL,
    ciudad character varying(255),
    codigo_postal character varying(255),
    direccion character varying(255),
    pais character varying(255),
    estado_pago character varying(20),
    mercadopago_preference_id character varying(255),
    mercadopago_payment_id character varying(255),
    metodo_pago character varying(50)
);


ALTER TABLE public.venta OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 16920)
-- Name: venta_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.venta ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.venta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 242 (class 1259 OID 16930)
-- Name: venta_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venta_item (
    id bigint NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario numeric(10,2),
    producto_id bigint NOT NULL,
    venta_id bigint NOT NULL
);


ALTER TABLE public.venta_item OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 16929)
-- Name: venta_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.venta_item ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.venta_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3369 (class 2604 OID 16968)
-- Name: checkout_pendiente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.checkout_pendiente ALTER COLUMN id SET DEFAULT nextval('public.checkout_pendiente_id_seq'::regclass);


--
-- TOC entry 3606 (class 0 OID 16965)
-- Dependencies: 244
-- Data for Name: checkout_pendiente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.checkout_pendiente VALUES (10, '3006513216-37366693-8952-4944-a7f1-65aa48ef682a', 2, '16996', 'Av. Principal 123', 'Lima', 'Perú', '15001', 179.80, 'PENDING', '2025-11-22 07:31:39.829597', '2025-11-23 07:31:39.829612');
INSERT INTO public.checkout_pendiente VALUES (11, '3006513216-cff83281-c7e5-46cc-97a8-df6de8464688', 2, '16999', 'Av. Principal 123', 'Lima', 'Perú', '15001', 89.90, 'COMPLETED', '2025-11-22 07:37:08.002805', '2025-11-23 07:37:08.002818');
INSERT INTO public.checkout_pendiente VALUES (12, '3006513216-f364187c-c230-4148-a661-499c530f79de', 2, '17002', 'Av. Metropolitana 123', 'Lima', 'Peru', '15471', 45.00, 'COMPLETED', '2025-11-22 08:47:08.698471', '2025-11-23 08:47:08.698482');
INSERT INTO public.checkout_pendiente VALUES (13, '3006513216-53f6b9a8-b9e2-40d6-a5d0-eb306090ada4', 2, '17005', 'Av. Metropolitana 123', 'Lima', 'Peru', '15471', 35.00, 'COMPLETED', '2025-11-22 08:57:05.50649', '2025-11-23 08:57:05.5065');
INSERT INTO public.checkout_pendiente VALUES (15, '3006513216-c9dc094a-39d9-4f1b-9cd1-d66963e84475', 2, '17009', 'Av. Metropolitana 178', 'Lima', 'Peru', '15471', 35.00, 'COMPLETED', '2025-11-22 09:13:44.857089', '2025-11-23 09:13:44.857101');
INSERT INTO public.checkout_pendiente VALUES (17, '3006513216-c35934d0-1768-4259-b0bb-a36cea0e51a8', 2, '17012', 'Av. Principal 123', 'Lima', 'Perú', '15001', 179.80, 'PENDING', '2025-11-22 09:28:52.486088', '2025-11-23 09:28:52.486101');
INSERT INTO public.checkout_pendiente VALUES (19, '3006513216-75c42024-75a2-44bc-91f9-7cbae3fc10cc', 2, '17015', 'Av. Principal 123', 'Lima', 'Perú', '15001', 179.80, 'PENDING', '2025-11-22 09:36:42.5017', '2025-11-23 09:36:42.501712');
INSERT INTO public.checkout_pendiente VALUES (20, '3006513216-75c17622-2b3d-4100-8ff6-6db447bfda86', 2, '17018', 'Av. Rimac 167', 'Lima', 'Peru', '15471', 12.50, 'COMPLETED', '2025-11-22 09:50:33.861805', '2025-11-23 09:50:33.861825');
INSERT INTO public.checkout_pendiente VALUES (21, '3006513216-32f81305-f089-4165-b880-5bc84cc338ba', 2, '17021', 'PSJ. Lima 147', 'Lima', 'Peru', '15471', 12.50, 'COMPLETED', '2025-11-22 10:14:22.045669', '2025-11-23 10:14:22.045681');
INSERT INTO public.checkout_pendiente VALUES (22, '3006513216-b26f49ff-291a-47a0-9065-c53b9ad51fcd', 2, '17023', 'Av. Comas 147', 'Lima', 'Peru', '15471', 12.50, 'PENDING', '2025-11-22 10:17:50.485566', '2025-11-23 10:17:50.485575');
INSERT INTO public.checkout_pendiente VALUES (23, '3006513216-01299839-1561-491f-9194-18c2e20308cf', 2, '17025', 'Avb. SA', 'Lima', 'Peru', '15471', 12.50, 'PENDING', '2025-11-24 18:13:02.676144', '2025-11-25 18:13:02.676159');
INSERT INTO public.checkout_pendiente VALUES (24, '3006513216-871f4188-5020-41d0-ba1f-57042b3e16a2', 2, '17028', 'AV. SA', 'ciudad1', 'pais1', '15471', 12.50, 'COMPLETED', '2025-11-24 18:16:56.129251', '2025-11-25 18:16:56.129268');
INSERT INTO public.checkout_pendiente VALUES (25, '3006513216-c2073d77-0773-4684-897a-b552dc322281', 2, '17030', 'dir123', 'ciudad1', 'pais1', '15471', 12.50, 'PENDING', '2025-12-02 09:18:44.650533', '2025-12-03 09:18:44.650547');
INSERT INTO public.checkout_pendiente VALUES (28, '3006513216-ff6d2ac7-71d8-43e2-b964-a253a4f88c90', 2, '17056', 'Av. Larco 123', 'Lima', 'Perú', '15074', 12.50, 'PENDING', '2025-12-03 21:32:23.674525', '2025-12-04 21:32:23.674537');
INSERT INTO public.checkout_pendiente VALUES (27, '3006513216-47d48153-abf3-4f1d-962a-21fab5388e93', 2, '17057', 'Av. Larco 123', 'Lima', 'Perú', '15074', 12.50, 'COMPLETED', '2025-12-03 21:29:53.919051', '2025-12-04 21:29:53.91906');
INSERT INTO public.checkout_pendiente VALUES (29, '725929146-2eb031c3-4de3-40e0-b727-c90a5641e1e0', 2, '17059', 'Av. Larco 123', 'Lima', 'Perú', '15074', 12.50, 'PENDING', '2025-12-03 21:56:23.44451', '2025-12-04 21:56:23.444524');
INSERT INTO public.checkout_pendiente VALUES (30, '725929146-0f6a3416-a67c-4570-b23f-2eec2b82e70e', 2, '17061', 'Av. Larco 123', 'Lima', 'Perú', '15074', 12.50, 'PENDING', '2025-12-03 21:58:52.591593', '2025-12-04 21:58:52.591602');
INSERT INTO public.checkout_pendiente VALUES (31, '3006513216-249fd5c7-fe73-4279-b229-cfdea98f24a0', 2, '17063', 'Av. Larco 123', 'Lima', 'Perú', '15074', 12.50, 'PENDING', '2025-12-03 22:06:48.865755', '2025-12-04 22:06:48.865768');
INSERT INTO public.checkout_pendiente VALUES (32, '3006513216-baf84776-203b-4cf8-add8-3c7ac8452527', 2, '17066', 'Av. Larco 123', 'Lima', 'Perú', '15074', 12.50, 'COMPLETED', '2025-12-03 22:09:25.747643', '2025-12-04 22:09:25.747656');
INSERT INTO public.checkout_pendiente VALUES (26, '3006513216-97798795-54cf-4c4e-8094-7e756a8661dc', 2, '17067', 'Av. Larco 123', 'Lima', 'Perú', '15074', 12.50, 'COMPLETED', '2025-12-03 21:26:44.0489', '2025-12-04 21:26:44.048915');
INSERT INTO public.checkout_pendiente VALUES (33, '3006513216-3ed00de3-91a2-4f59-b60f-dea3f7dd838f', 2, '17097', 'av. los proceres 197', 'lima', 'peru', '15471', 12.50, 'PENDING', '2025-12-04 05:52:54.709293', '2025-12-05 05:52:54.709305');
INSERT INTO public.checkout_pendiente VALUES (34, '3006513216-6bbda375-a69c-4833-9017-13e42d594a72', 2, '17099', 'Av. los proceres 178', 'lima', 'Peru', '15471', 12.50, 'PENDING', '2025-12-04 05:56:38.000826', '2025-12-05 05:56:38.000837');
INSERT INTO public.checkout_pendiente VALUES (35, '3006513216-dae839d2-36d3-4f59-a2f8-9015379460a7', 2, '17101', 'av. casa 1234', 'lima', 'peru', '14797', 12.50, 'PENDING', '2025-12-04 05:59:43.255925', '2025-12-05 05:59:43.255935');
INSERT INTO public.checkout_pendiente VALUES (36, '3006513216-ff3e17b9-0857-4ebb-aec5-2683c18a5035', 2, '17103', 'av casa 214', 'lima', 'peru', '14234', 12.50, 'PENDING', '2025-12-04 06:01:12.177103', '2025-12-05 06:01:12.177115');
INSERT INTO public.checkout_pendiente VALUES (37, '3006513216-100bc4cf-c5de-40ad-b53b-5708bb7666ba', 2, '17105', 'av casa 1298', 'Lima', 'Peru', '14757', 12.50, 'PENDING', '2025-12-04 06:16:53.612664', '2025-12-05 06:16:53.612674');
INSERT INTO public.checkout_pendiente VALUES (38, '3006513216-604009ce-9539-4ac6-bcfe-e1bde31cc59f', 2, '17108', 'Av. Principal 123', 'lima', 'Peru', '110111', 12.50, 'COMPLETED', '2025-12-04 06:32:20.804046', '2025-12-05 06:32:20.804058');
INSERT INTO public.checkout_pendiente VALUES (39, '3006513216-4f3f50af-401d-4b05-9a7f-2a28c4907478', 2, '17111', 'av. los angeles 2222', 'lima', 'peru', '14789', 12.50, 'COMPLETED', '2025-12-04 06:40:19.605495', '2025-12-05 06:40:19.605506');


--
-- TOC entry 3584 (class 0 OID 16773)
-- Dependencies: 222
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.cliente OVERRIDING SYSTEM VALUE VALUES (2, 3, 'cliente1', 'cliente1', 'cliente1', '47887457', '848748951', 'cliente1@cliente.com', '2009-02-04', true, NULL, NULL, NULL, NULL);
INSERT INTO public.cliente OVERRIDING SYSTEM VALUE VALUES (1, 2, 'cliente', 'c', 'l', '98745412', '987456512', '123@correo.pe', '2004-08-07', false, 'ciudad1', '15471', 'dir1', 'pais1');


--
-- TOC entry 3596 (class 0 OID 16869)
-- Dependencies: 234
-- Data for Name: compra; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.compra OVERRIDING SYSTEM VALUE VALUES (44, 1, 2250.00, 405.00, 2655.00, '2025-10-25 13:29:49.892905');


--
-- TOC entry 3598 (class 0 OID 16885)
-- Dependencies: 236
-- Data for Name: compra_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.compra_item OVERRIDING SYSTEM VALUE VALUES (1, 44, 2, 150.00, 15, 2250.00);


--
-- TOC entry 3600 (class 0 OID 16902)
-- Dependencies: 238
-- Data for Name: comprobante; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.comprobante VALUES (1, '2025-11-01 15:22:57.009329', 13.71, 'PEN', '00000001', '10203040501', 'B001', 76.19, 89.90, 1);
INSERT INTO public.comprobante VALUES (2, '2025-11-01 15:23:50.824933', 45.61, 'PEN', '00000002', '10203040501', 'B001', 253.39, 299.00, 2);
INSERT INTO public.comprobante VALUES (3, '2025-11-13 03:49:05.909435', 6.86, 'PEN', '00000029', '10203040501', 'B001', 38.14, 45.00, 29);
INSERT INTO public.comprobante VALUES (4, '2025-11-14 04:26:32.762846', 13.71, 'PEN', '00000030', '10203040501', 'B001', 76.19, 89.90, 30);
INSERT INTO public.comprobante VALUES (5, '2025-11-22 07:39:48.858895', 13.71, 'PEN', '00000031', '10203040501', 'B001', 76.19, 89.90, 31);
INSERT INTO public.comprobante VALUES (6, '2025-11-22 08:54:34.312657', 6.86, 'PEN', '00000032', '10203040501', 'B001', 38.14, 45.00, 32);
INSERT INTO public.comprobante VALUES (7, '2025-11-22 08:57:44.481441', 5.34, 'PEN', '00000033', '10203040501', 'B001', 29.66, 35.00, 33);
INSERT INTO public.comprobante VALUES (8, '2025-11-22 09:13:56.747614', 5.34, 'PEN', '00000034', '10203040501', 'B001', 29.66, 35.00, 34);
INSERT INTO public.comprobante VALUES (9, '2025-11-22 09:51:28.282927', 1.91, 'PEN', '00000035', '10203040501', 'B001', 10.59, 12.50, 35);
INSERT INTO public.comprobante VALUES (10, '2025-11-22 10:15:16.667379', 1.91, 'PEN', '00000036', '10203040501', 'B001', 10.59, 12.50, 36);
INSERT INTO public.comprobante VALUES (11, '2025-11-24 18:17:36.266129', 1.91, 'PEN', '00000037', '10203040501', 'B001', 10.59, 12.50, 37);
INSERT INTO public.comprobante VALUES (12, '2025-12-03 21:33:20.622739', 1.91, 'PEN', '00000038', '10203040501', 'B001', 10.59, 12.50, 38);
INSERT INTO public.comprobante VALUES (13, '2025-12-03 22:10:09.943593', 1.91, 'PEN', '00000039', '10203040501', 'B001', 10.59, 12.50, 39);
INSERT INTO public.comprobante VALUES (14, '2025-12-03 22:20:53.304538', 1.91, 'PEN', '00000040', '10203040501', 'B001', 10.59, 12.50, 40);
INSERT INTO public.comprobante VALUES (15, '2025-12-04 06:34:21.707569', 1.91, 'PEN', '00000041', '10203040501', 'B001', 10.59, 12.50, 41);
INSERT INTO public.comprobante VALUES (16, '2025-12-04 06:40:29.817196', 1.91, 'PEN', '00000042', '10203040501', 'B001', 10.59, 12.50, 42);


--
-- TOC entry 3586 (class 0 OID 16791)
-- Dependencies: 224
-- Data for Name: empleado; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.empleado OVERRIDING SYSTEM VALUE VALUES (1, 1, 'Administrador', NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, true);


--
-- TOC entry 3592 (class 0 OID 16834)
-- Dependencies: 230
-- Data for Name: pedido; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3594 (class 0 OID 16852)
-- Dependencies: 232
-- Data for Name: pedido_item; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3590 (class 0 OID 16818)
-- Dependencies: 228
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (3, 'Mascarilla respirador 3M 6200', 'Respirador reutilizable con cartuchos intercambiables para vapores y gases', 149.00, 80, '3M', 'Protección respiratoria', 2, 'https://alaska.pe/wp-content/uploads/2023/07/Respirador-media-cara-6200-3M.jpg', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (5, 'Botas dieléctricas PVC', 'Botas impermeables con puntera de acero, resistentes a descargas eléctricas', 179.00, 40, 'Segurimax', 'Protección podal', 4, 'https://promart.vteximg.com.br/arquivos/ids/7879561-700-700/11308.jpg?v=638455440556870000', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (7, 'Orejeras 3M Peltor Optime 105', 'Orejeras con aislamiento acústico de alta atenuación', 99.00, 70, '3M', 'Protección auditiva', 2, 'https://media.falabella.com/falabellaPE/127680107_01/w=1500,h=1500,fit=pad', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (9, 'Pantalla facial completa', 'Visor facial de policarbonato con banda ajustable para protección contra salpicaduras', 59.00, 90, 'Andina Safety', 'Protección facial', 1, 'https://siasuministros.com/1834748-big_default/pantalla-facial-superface-transparente-safetop-79300.jpg', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (10, 'Cono de seguridad 75 cm', 'Cono naranja de PVC con base negra y cinta reflectiva', 69.00, 100, 'SafeWork', 'Señalización y control', 5, 'https://www.segutecnica.com/images/000000000000000091041dps-cono-pe-base-de-goma-economico-750-rigido-2-bandas-reflectivas-segutecnica.png', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (6, 'Arnés de seguridad de cuerpo completo', 'Arnés con 5 puntos de anclaje y hebillas de acero galvanizado', 299.00, 24, 'SafeWork', 'Protección anticaídas', 5, 'https://m.media-amazon.com/images/I/81QgUXgRLtL._AC_SL1500_.jpg', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (2, 'Guantes de nitrilo reforzado', 'Guantes resistentes a químicos, abrasión y cortes leves', 12.50, 209, '3M', 'Protección de manos', 2, 'https://www.segutecnica.com/images/000000000001598730025steelpro-guantes-multiflex-nitrilo-espumado-respirable-30100078064-segutecnica.png', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (1, 'Casco de seguridad MSA V-Gard', 'Casco dieléctrico tipo I con suspensión Fas-Trac III', 89.90, 57, 'MSA', 'Protección craneal', 1, 'https://s7d9.scene7.com/is/image/minesafetyappliances/V-GardHardHatCapStyle_000060001300001000?$Home%20Market%20Card$', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (4, 'Lentes de seguridad UVEX Astrospec', 'Lentes de policarbonato antiempañantes y protección UV', 45.00, 118, 'UVEX', 'Protección ocular', 3, 'https://b2btreckpe.vtexassets.com/arquivos/ids/174468-800-auto?v=638893239725070000&width=800&height=auto&aspect=true', true);
INSERT INTO public.producto OVERRIDING SYSTEM VALUE VALUES (8, 'Chaleco reflectivo clase 2', 'Chaleco de seguridad color verde fosforescente con cintas reflectivas', 35.00, 148, 'Protek', 'Ropa de seguridad', 2, 'https://seguridadglobal.com.ar/wp-content/uploads/2022/01/Chaleco-reflectivo-Clase-2-Prisma.jpg', true);


--
-- TOC entry 3588 (class 0 OID 16809)
-- Dependencies: 226
-- Data for Name: proveedor; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.proveedor OVERRIDING SYSTEM VALUE VALUES (1, 'Seguridad Industrial Andina S.A.C.', '20604587231', '987654321', 'contacto@segandina.com', true);
INSERT INTO public.proveedor OVERRIDING SYSTEM VALUE VALUES (2, 'Protek Perú E.I.R.L.', '20587456321', '956123789', 'ventas@protekperu.com', true);
INSERT INTO public.proveedor OVERRIDING SYSTEM VALUE VALUES (3, 'Distribuidora EPP Global S.R.L.', '20457896542', '999888777', 'info@eppglobal.com', true);
INSERT INTO public.proveedor OVERRIDING SYSTEM VALUE VALUES (4, 'Industrias Segurimax S.A.', '20607896510', '912345678', 'comercial@segurimax.com', true);
INSERT INTO public.proveedor OVERRIDING SYSTEM VALUE VALUES (5, 'Importadora SafeWork S.A.C.', '20369874125', '987321654', 'soporte@safework.com', true);


--
-- TOC entry 3608 (class 0 OID 17069)
-- Dependencies: 246
-- Data for Name: solicitud_reembolso; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.solicitud_reembolso VALUES (1, 'Reembolso aprobado según política de la empresa', 'APROBADO', '2025-12-04 01:48:21.490818', '2025-12-04 01:23:09.092279', 'El producto llegó dañado', 2, 1, 40);
INSERT INTO public.solicitud_reembolso VALUES (2, 'No se puede realizar el reembolso de su producto debido a su falta adicional en su descripción.', 'RECHAZADO', '2025-12-04 04:35:34.561296', '2025-12-04 03:48:21.159311', 'El producto esta dañado y vino abierto.', 2, 1, 39);
INSERT INTO public.solicitud_reembolso VALUES (3, 'El motivo fue analizado y se aprobó el reembolso.', 'APROBADO', '2025-12-04 06:43:47.20539', '2025-12-04 06:42:29.341719', 'El producto esta vino roto.', 2, 1, 42);


--
-- TOC entry 3582 (class 0 OID 16764)
-- Dependencies: 220
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.usuario OVERRIDING SYSTEM VALUE VALUES (3, 'cliente1', '$2a$10$sMgVwnOpzidbwXJuoKl3aOGI1wpovyVYRNbpBFtsLKb7unLzlY/lG', true);
INSERT INTO public.usuario OVERRIDING SYSTEM VALUE VALUES (1, 'admin', '$2a$12$wnvvQE8N2vpsBOojXqCzk.QB3dgHLR7hJRnxu5kRej6IQJ2HepzL6', true);
INSERT INTO public.usuario OVERRIDING SYSTEM VALUE VALUES (2, 'cliente', 'cliente', false);


--
-- TOC entry 3602 (class 0 OID 16921)
-- Dependencies: 240
-- Data for Name: venta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.venta VALUES (1, '2025-11-01 15:22:56.992195', 89.90, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.venta VALUES (2, '2025-11-01 15:23:50.823216', 299.00, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.venta VALUES (8, '2025-11-12 21:14:09.543691', 45.00, 2, 'ciudad1', '15471', 'dir1', 'pais1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (9, '2025-11-12 21:17:43.912323', 99.00, 2, 'ciudad1', '15471', 'dir1', 'pais1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (10, '2025-11-12 21:25:51.66823', 12.50, 2, 'ciudad1', '15471', 'dir1', 'pais1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (11, '2025-11-12 21:28:13.046005', 35.00, 2, 'ciudad1', '15471', 'dir1', 'pais1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (12, '2025-11-12 21:35:05.668378', 59.00, 2, 'ciu1', '15328', 'dir1', 'per1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (13, '2025-11-12 21:47:12.476921', 99.00, 2, 'ciu1', '15328', 'dir1', 'per1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (14, '2025-11-12 22:02:16.204341', 179.00, 2, 'ciudad1', '15471', 'dir1', 'pais1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (15, '2025-11-12 22:06:42.48503', 69.00, 2, 'ciu1', '15328', 'dir1', 'per1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (16, '2025-11-12 22:11:23.397598', 69.00, 2, 'ciu1', '15328', 'dir1', 'per1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (17, '2025-11-12 23:10:10.889817', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (18, '2025-11-12 23:17:32.287602', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (19, '2025-11-12 23:28:59.212894', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (20, '2025-11-12 23:39:33.584611', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (21, '2025-11-12 23:51:02.004036', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (26, '2025-11-13 00:37:34.335669', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (27, '2025-11-13 01:37:44.790662', 149.00, 2, 'ciu1', '15328', 'dir1', 'per1', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (28, '2025-11-13 01:53:27.976491', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'PENDIENTE', NULL, NULL, NULL);
INSERT INTO public.venta VALUES (29, '2025-11-13 03:49:05.896291', 45.00, 2, 'ciudad1', '15471', 'dir1', 'pais1', NULL, NULL, NULL, NULL);
INSERT INTO public.venta VALUES (30, '2025-11-14 04:26:32.745597', 89.90, 2, 'ciudad1', '15471', 'dir2131', 'pais1', NULL, NULL, NULL, NULL);
INSERT INTO public.venta VALUES (31, '2025-11-22 07:39:48.849802', 89.90, 2, 'Lima', '15001', 'Av. Principal 123', 'Perú', 'approved', '3006513216-cff83281-c7e5-46cc-97a8-df6de8464688', '134210993989', 'account_money');
INSERT INTO public.venta VALUES (32, '2025-11-22 08:54:34.296488', 45.00, 2, 'Lima', '15471', 'Av. Metropolitana 123', 'Peru', 'approved', '3006513216-f364187c-c230-4148-a661-499c530f79de', '134825948042', 'account_money');
INSERT INTO public.venta VALUES (33, '2025-11-22 08:57:44.479324', 35.00, 2, 'Lima', '15471', 'Av. Metropolitana 123', 'Peru', 'approved', '3006513216-53f6b9a8-b9e2-40d6-a5d0-eb306090ada4', '134826235178', 'account_money');
INSERT INTO public.venta VALUES (34, '2025-11-22 09:13:56.741711', 35.00, 2, 'Lima', '15471', 'Av. Metropolitana 178', 'Peru', 'approved', '3006513216-c9dc094a-39d9-4f1b-9cd1-d66963e84475', '134826522372', 'account_money');
INSERT INTO public.venta VALUES (35, '2025-11-22 09:51:28.277029', 12.50, 2, 'Lima', '15471', 'Av. Rimac 167', 'Peru', 'approved', '3006513216-75c17622-2b3d-4100-8ff6-6db447bfda86', '134213220297', 'account_money');
INSERT INTO public.venta VALUES (36, '2025-11-22 10:15:16.661565', 12.50, 2, 'Lima', '15471', 'PSJ. Lima 147', 'Peru', 'approved', '3006513216-32f81305-f089-4165-b880-5bc84cc338ba', '134827852030', 'account_money');
INSERT INTO public.venta VALUES (37, '2025-11-24 18:17:36.256696', 12.50, 2, 'ciudad1', '15471', 'AV. SA', 'pais1', 'approved', '3006513216-871f4188-5020-41d0-ba1f-57042b3e16a2', '134474649757', 'account_money');
INSERT INTO public.venta VALUES (38, '2025-12-03 21:33:20.615898', 12.50, 2, 'Lima', '15074', 'Av. Larco 123', 'Perú', 'approved', '3006513216-47d48153-abf3-4f1d-962a-21fab5388e93', '135721164325', 'account_money');
INSERT INTO public.venta VALUES (39, '2025-12-03 22:10:09.937849', 12.50, 2, 'Lima', '15074', 'Av. Larco 123', 'Perú', 'approved', '3006513216-baf84776-203b-4cf8-add8-3c7ac8452527', '135726624785', 'account_money');
INSERT INTO public.venta VALUES (40, '2025-12-03 22:20:53.302637', 12.50, 2, 'Lima', '15074', 'Av. Larco 123', 'Perú', 'REEMBOLSADO', '3006513216-97798795-54cf-4c4e-8094-7e756a8661dc', '136341331448', 'account_money');
INSERT INTO public.venta VALUES (41, '2025-12-04 06:34:21.700979', 12.50, 2, 'lima', '110111', 'Av. Principal 123', 'Peru', 'approved', '3006513216-604009ce-9539-4ac6-bcfe-e1bde31cc59f', '136391608170', 'account_money');
INSERT INTO public.venta VALUES (42, '2025-12-04 06:40:29.815402', 12.50, 2, 'lima', '14789', 'av. los angeles 2222', 'peru', 'REEMBOLSADO', '3006513216-4f3f50af-401d-4b05-9a7f-2a28c4907478', '136391617300', 'account_money');


--
-- TOC entry 3604 (class 0 OID 16930)
-- Dependencies: 242
-- Data for Name: venta_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.venta_item VALUES (1, 1, 89.90, 1, 1);
INSERT INTO public.venta_item VALUES (2, 1, 299.00, 6, 2);
INSERT INTO public.venta_item VALUES (8, 1, 45.00, 4, 8);
INSERT INTO public.venta_item VALUES (9, 1, 99.00, 7, 9);
INSERT INTO public.venta_item VALUES (10, 1, 12.50, 2, 10);
INSERT INTO public.venta_item VALUES (11, 1, 35.00, 8, 11);
INSERT INTO public.venta_item VALUES (12, 1, 59.00, 9, 12);
INSERT INTO public.venta_item VALUES (13, 1, 99.00, 7, 13);
INSERT INTO public.venta_item VALUES (14, 1, 179.00, 5, 14);
INSERT INTO public.venta_item VALUES (15, 1, 69.00, 10, 15);
INSERT INTO public.venta_item VALUES (16, 1, 69.00, 10, 16);
INSERT INTO public.venta_item VALUES (17, 1, 89.90, 1, 17);
INSERT INTO public.venta_item VALUES (18, 1, 89.90, 1, 18);
INSERT INTO public.venta_item VALUES (19, 1, 89.90, 1, 19);
INSERT INTO public.venta_item VALUES (20, 1, 89.90, 1, 20);
INSERT INTO public.venta_item VALUES (21, 1, 89.90, 1, 21);
INSERT INTO public.venta_item VALUES (26, 1, 89.90, 1, 26);
INSERT INTO public.venta_item VALUES (27, 1, 149.00, 3, 27);
INSERT INTO public.venta_item VALUES (28, 1, 89.90, 1, 28);
INSERT INTO public.venta_item VALUES (29, 1, 45.00, 4, 29);
INSERT INTO public.venta_item VALUES (30, 1, 89.90, 1, 30);
INSERT INTO public.venta_item VALUES (31, 1, 89.90, 1, 31);
INSERT INTO public.venta_item VALUES (32, 1, 45.00, 4, 32);
INSERT INTO public.venta_item VALUES (33, 1, 35.00, 8, 33);
INSERT INTO public.venta_item VALUES (34, 1, 35.00, 8, 34);
INSERT INTO public.venta_item VALUES (35, 1, 12.50, 2, 35);
INSERT INTO public.venta_item VALUES (36, 1, 12.50, 2, 36);
INSERT INTO public.venta_item VALUES (37, 1, 12.50, 2, 37);
INSERT INTO public.venta_item VALUES (38, 1, 12.50, 2, 38);
INSERT INTO public.venta_item VALUES (39, 1, 12.50, 2, 39);
INSERT INTO public.venta_item VALUES (40, 1, 12.50, 2, 40);
INSERT INTO public.venta_item VALUES (41, 1, 12.50, 2, 41);
INSERT INTO public.venta_item VALUES (42, 1, 12.50, 2, 42);


--
-- TOC entry 3618 (class 0 OID 0)
-- Dependencies: 243
-- Name: checkout_pendiente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.checkout_pendiente_id_seq', 39, true);


--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 221
-- Name: cliente_id_cliente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cliente_id_cliente_seq', 2, true);


--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 233
-- Name: compra_id_compra_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.compra_id_compra_seq', 44, true);


--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 235
-- Name: compra_item_id_compra_item_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.compra_item_id_compra_item_seq', 1, true);


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 237
-- Name: comprobante_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comprobante_id_seq', 16, true);


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 223
-- Name: empleado_id_empleado_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.empleado_id_empleado_seq', 1, true);


--
-- TOC entry 3624 (class 0 OID 0)
-- Dependencies: 229
-- Name: pedido_id_pedido_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pedido_id_pedido_seq', 1, false);


--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 231
-- Name: pedido_item_id_pedido_item_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.pedido_item_id_pedido_item_seq', 1, false);


--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 227
-- Name: producto_id_producto_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.producto_id_producto_seq', 10, true);


--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 225
-- Name: proveedor_id_proveedor_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.proveedor_id_proveedor_seq', 5, true);


--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 245
-- Name: solicitud_reembolso_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.solicitud_reembolso_id_seq', 3, true);


--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 219
-- Name: usuario_id_usuario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_id_usuario_seq', 3, true);


--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 239
-- Name: venta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.venta_id_seq', 42, true);


--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 241
-- Name: venta_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.venta_item_id_seq', 42, true);


--
-- TOC entry 3609 (class 2613 OID 16995)
-- Name: 16995..17111; Type: BLOB METADATA; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16995');
SELECT pg_catalog.lo_create('16996');
SELECT pg_catalog.lo_create('16997');
SELECT pg_catalog.lo_create('16998');
SELECT pg_catalog.lo_create('16999');
SELECT pg_catalog.lo_create('17000');
SELECT pg_catalog.lo_create('17001');
SELECT pg_catalog.lo_create('17002');
SELECT pg_catalog.lo_create('17003');
SELECT pg_catalog.lo_create('17004');
SELECT pg_catalog.lo_create('17005');
SELECT pg_catalog.lo_create('17007');
SELECT pg_catalog.lo_create('17008');
SELECT pg_catalog.lo_create('17009');
SELECT pg_catalog.lo_create('17011');
SELECT pg_catalog.lo_create('17012');
SELECT pg_catalog.lo_create('17014');
SELECT pg_catalog.lo_create('17015');
SELECT pg_catalog.lo_create('17016');
SELECT pg_catalog.lo_create('17017');
SELECT pg_catalog.lo_create('17018');
SELECT pg_catalog.lo_create('17019');
SELECT pg_catalog.lo_create('17020');
SELECT pg_catalog.lo_create('17021');
SELECT pg_catalog.lo_create('17022');
SELECT pg_catalog.lo_create('17023');
SELECT pg_catalog.lo_create('17024');
SELECT pg_catalog.lo_create('17025');
SELECT pg_catalog.lo_create('17026');
SELECT pg_catalog.lo_create('17027');
SELECT pg_catalog.lo_create('17028');
SELECT pg_catalog.lo_create('17029');
SELECT pg_catalog.lo_create('17030');
SELECT pg_catalog.lo_create('17051');
SELECT pg_catalog.lo_create('17052');
SELECT pg_catalog.lo_create('17053');
SELECT pg_catalog.lo_create('17054');
SELECT pg_catalog.lo_create('17055');
SELECT pg_catalog.lo_create('17056');
SELECT pg_catalog.lo_create('17057');
SELECT pg_catalog.lo_create('17058');
SELECT pg_catalog.lo_create('17059');
SELECT pg_catalog.lo_create('17060');
SELECT pg_catalog.lo_create('17061');
SELECT pg_catalog.lo_create('17062');
SELECT pg_catalog.lo_create('17063');
SELECT pg_catalog.lo_create('17064');
SELECT pg_catalog.lo_create('17065');
SELECT pg_catalog.lo_create('17066');
SELECT pg_catalog.lo_create('17067');
SELECT pg_catalog.lo_create('17096');
SELECT pg_catalog.lo_create('17097');
SELECT pg_catalog.lo_create('17098');
SELECT pg_catalog.lo_create('17099');
SELECT pg_catalog.lo_create('17100');
SELECT pg_catalog.lo_create('17101');
SELECT pg_catalog.lo_create('17102');
SELECT pg_catalog.lo_create('17103');
SELECT pg_catalog.lo_create('17104');
SELECT pg_catalog.lo_create('17105');
SELECT pg_catalog.lo_create('17106');
SELECT pg_catalog.lo_create('17107');
SELECT pg_catalog.lo_create('17108');
SELECT pg_catalog.lo_create('17109');
SELECT pg_catalog.lo_create('17110');
SELECT pg_catalog.lo_create('17111');

ALTER LARGE OBJECT 16995 OWNER TO postgres;
ALTER LARGE OBJECT 16996 OWNER TO postgres;
ALTER LARGE OBJECT 16997 OWNER TO postgres;
ALTER LARGE OBJECT 16998 OWNER TO postgres;
ALTER LARGE OBJECT 16999 OWNER TO postgres;
ALTER LARGE OBJECT 17000 OWNER TO postgres;
ALTER LARGE OBJECT 17001 OWNER TO postgres;
ALTER LARGE OBJECT 17002 OWNER TO postgres;
ALTER LARGE OBJECT 17003 OWNER TO postgres;
ALTER LARGE OBJECT 17004 OWNER TO postgres;
ALTER LARGE OBJECT 17005 OWNER TO postgres;
ALTER LARGE OBJECT 17007 OWNER TO postgres;
ALTER LARGE OBJECT 17008 OWNER TO postgres;
ALTER LARGE OBJECT 17009 OWNER TO postgres;
ALTER LARGE OBJECT 17011 OWNER TO postgres;
ALTER LARGE OBJECT 17012 OWNER TO postgres;
ALTER LARGE OBJECT 17014 OWNER TO postgres;
ALTER LARGE OBJECT 17015 OWNER TO postgres;
ALTER LARGE OBJECT 17016 OWNER TO postgres;
ALTER LARGE OBJECT 17017 OWNER TO postgres;
ALTER LARGE OBJECT 17018 OWNER TO postgres;
ALTER LARGE OBJECT 17019 OWNER TO postgres;
ALTER LARGE OBJECT 17020 OWNER TO postgres;
ALTER LARGE OBJECT 17021 OWNER TO postgres;
ALTER LARGE OBJECT 17022 OWNER TO postgres;
ALTER LARGE OBJECT 17023 OWNER TO postgres;
ALTER LARGE OBJECT 17024 OWNER TO postgres;
ALTER LARGE OBJECT 17025 OWNER TO postgres;
ALTER LARGE OBJECT 17026 OWNER TO postgres;
ALTER LARGE OBJECT 17027 OWNER TO postgres;
ALTER LARGE OBJECT 17028 OWNER TO postgres;
ALTER LARGE OBJECT 17029 OWNER TO postgres;
ALTER LARGE OBJECT 17030 OWNER TO postgres;
ALTER LARGE OBJECT 17051 OWNER TO postgres;
ALTER LARGE OBJECT 17052 OWNER TO postgres;
ALTER LARGE OBJECT 17053 OWNER TO postgres;
ALTER LARGE OBJECT 17054 OWNER TO postgres;
ALTER LARGE OBJECT 17055 OWNER TO postgres;
ALTER LARGE OBJECT 17056 OWNER TO postgres;
ALTER LARGE OBJECT 17057 OWNER TO postgres;
ALTER LARGE OBJECT 17058 OWNER TO postgres;
ALTER LARGE OBJECT 17059 OWNER TO postgres;
ALTER LARGE OBJECT 17060 OWNER TO postgres;
ALTER LARGE OBJECT 17061 OWNER TO postgres;
ALTER LARGE OBJECT 17062 OWNER TO postgres;
ALTER LARGE OBJECT 17063 OWNER TO postgres;
ALTER LARGE OBJECT 17064 OWNER TO postgres;
ALTER LARGE OBJECT 17065 OWNER TO postgres;
ALTER LARGE OBJECT 17066 OWNER TO postgres;
ALTER LARGE OBJECT 17067 OWNER TO postgres;
ALTER LARGE OBJECT 17096 OWNER TO postgres;
ALTER LARGE OBJECT 17097 OWNER TO postgres;
ALTER LARGE OBJECT 17098 OWNER TO postgres;
ALTER LARGE OBJECT 17099 OWNER TO postgres;
ALTER LARGE OBJECT 17100 OWNER TO postgres;
ALTER LARGE OBJECT 17101 OWNER TO postgres;
ALTER LARGE OBJECT 17102 OWNER TO postgres;
ALTER LARGE OBJECT 17103 OWNER TO postgres;
ALTER LARGE OBJECT 17104 OWNER TO postgres;
ALTER LARGE OBJECT 17105 OWNER TO postgres;
ALTER LARGE OBJECT 17106 OWNER TO postgres;
ALTER LARGE OBJECT 17107 OWNER TO postgres;
ALTER LARGE OBJECT 17108 OWNER TO postgres;
ALTER LARGE OBJECT 17109 OWNER TO postgres;
ALTER LARGE OBJECT 17110 OWNER TO postgres;
ALTER LARGE OBJECT 17111 OWNER TO postgres;

--
-- TOC entry 3610 (class 0 OID 0)
-- Dependencies: 3609 3611
-- Data for Name: 16995..17111; Type: BLOBS; Schema: -; Owner: postgres
--

BEGIN;

SELECT pg_catalog.lo_open('16995', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a327d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16996', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a327d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16997', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16998', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16999', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17000', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a342c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17001', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a342c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17002', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a342c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17003', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a382c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17004', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a382c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17005', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a382c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17007', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a382c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17008', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a382c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17009', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a382c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17011', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a327d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17012', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a327d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17014', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a327d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17015', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a312c2263616e7469646164223a327d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17016', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17017', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17018', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17019', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17020', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17021', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17022', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17023', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17024', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17025', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17026', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17027', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17028', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17029', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17030', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17051', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17052', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17053', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17054', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17055', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17056', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17057', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17058', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17059', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17060', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17061', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17062', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17063', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17064', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17065', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17066', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17067', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17096', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17097', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17098', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17099', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17100', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17101', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17102', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17103', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17104', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17105', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17106', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17107', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17108', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17109', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17110', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('17111', 131072);
SELECT pg_catalog.lowrite(0, '\x5b7b2270726f647563746f4964223a322c2263616e7469646164223a317d5d');
SELECT pg_catalog.lo_close(0);

COMMIT;

--
-- TOC entry 3412 (class 2606 OID 16978)
-- Name: checkout_pendiente checkout_pendiente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.checkout_pendiente
    ADD CONSTRAINT checkout_pendiente_pkey PRIMARY KEY (id);


--
-- TOC entry 3414 (class 2606 OID 16980)
-- Name: checkout_pendiente checkout_pendiente_preference_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.checkout_pendiente
    ADD CONSTRAINT checkout_pendiente_preference_id_key UNIQUE (preference_id);


--
-- TOC entry 3378 (class 2606 OID 16457)
-- Name: cliente cliente_dni_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_dni_key UNIQUE (dni);


--
-- TOC entry 3380 (class 2606 OID 16458)
-- Name: cliente cliente_id_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_id_usuario_key UNIQUE (id_usuario);


--
-- TOC entry 3382 (class 2606 OID 16459)
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- TOC entry 3402 (class 2606 OID 16460)
-- Name: compra_item compra_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra_item
    ADD CONSTRAINT compra_item_pkey PRIMARY KEY (id_compra_item);


--
-- TOC entry 3400 (class 2606 OID 16461)
-- Name: compra compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra
    ADD CONSTRAINT compra_pkey PRIMARY KEY (id_compra);


--
-- TOC entry 3404 (class 2606 OID 16913)
-- Name: comprobante comprobante_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comprobante
    ADD CONSTRAINT comprobante_pkey PRIMARY KEY (id);


--
-- TOC entry 3384 (class 2606 OID 16462)
-- Name: empleado empleado_dni_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_dni_key UNIQUE (dni);


--
-- TOC entry 3386 (class 2606 OID 16463)
-- Name: empleado empleado_id_usuario_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_id_usuario_key UNIQUE (id_usuario);


--
-- TOC entry 3388 (class 2606 OID 16464)
-- Name: empleado empleado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_pkey PRIMARY KEY (id_empleado);


--
-- TOC entry 3398 (class 2606 OID 16465)
-- Name: pedido_item pedido_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido_item
    ADD CONSTRAINT pedido_item_pkey PRIMARY KEY (id_pedido_item);


--
-- TOC entry 3396 (class 2606 OID 16466)
-- Name: pedido pedido_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido
    ADD CONSTRAINT pedido_pkey PRIMARY KEY (id_pedido);


--
-- TOC entry 3394 (class 2606 OID 16467)
-- Name: producto producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (id_producto);


--
-- TOC entry 3390 (class 2606 OID 16468)
-- Name: proveedor proveedor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proveedor
    ADD CONSTRAINT proveedor_pkey PRIMARY KEY (id_proveedor);


--
-- TOC entry 3392 (class 2606 OID 16469)
-- Name: proveedor proveedor_ruc_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proveedor
    ADD CONSTRAINT proveedor_ruc_key UNIQUE (ruc);


--
-- TOC entry 3416 (class 2606 OID 17080)
-- Name: solicitud_reembolso solicitud_reembolso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solicitud_reembolso
    ADD CONSTRAINT solicitud_reembolso_pkey PRIMARY KEY (id);


--
-- TOC entry 3406 (class 2606 OID 16940)
-- Name: comprobante ukp7wy9r4antytf4jep3qthwpdo; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comprobante
    ADD CONSTRAINT ukp7wy9r4antytf4jep3qthwpdo UNIQUE (venta_id);


--
-- TOC entry 3374 (class 2606 OID 16470)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- TOC entry 3376 (class 2606 OID 16471)
-- Name: usuario usuario_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_username_key UNIQUE (username);


--
-- TOC entry 3410 (class 2606 OID 16938)
-- Name: venta_item venta_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_item
    ADD CONSTRAINT venta_item_pkey PRIMARY KEY (id);


--
-- TOC entry 3408 (class 2606 OID 16928)
-- Name: venta venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_pkey PRIMARY KEY (id);


--
-- TOC entry 3417 (class 2606 OID 16472)
-- Name: cliente cliente_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario) ON DELETE SET NULL;


--
-- TOC entry 3423 (class 2606 OID 16477)
-- Name: compra compra_id_proveedor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra
    ADD CONSTRAINT compra_id_proveedor_fkey FOREIGN KEY (id_proveedor) REFERENCES public.proveedor(id_proveedor) ON DELETE SET NULL;


--
-- TOC entry 3424 (class 2606 OID 16482)
-- Name: compra_item compra_item_id_compra_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra_item
    ADD CONSTRAINT compra_item_id_compra_fkey FOREIGN KEY (id_compra) REFERENCES public.compra(id_compra) ON DELETE CASCADE;


--
-- TOC entry 3425 (class 2606 OID 16487)
-- Name: compra_item compra_item_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra_item
    ADD CONSTRAINT compra_item_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 3418 (class 2606 OID 16492)
-- Name: empleado empleado_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario) ON DELETE SET NULL;


--
-- TOC entry 3430 (class 2606 OID 16981)
-- Name: checkout_pendiente fk_checkout_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.checkout_pendiente
    ADD CONSTRAINT fk_checkout_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id_cliente);


--
-- TOC entry 3427 (class 2606 OID 16946)
-- Name: venta fka7yaj59nfh3gft0h38o5bv472; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT fka7yaj59nfh3gft0h38o5bv472 FOREIGN KEY (cliente_id) REFERENCES public.cliente(id_cliente);


--
-- TOC entry 3431 (class 2606 OID 17091)
-- Name: solicitud_reembolso fkag40q3016flx3j2p5ou9et2fj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solicitud_reembolso
    ADD CONSTRAINT fkag40q3016flx3j2p5ou9et2fj FOREIGN KEY (venta_id) REFERENCES public.venta(id);


--
-- TOC entry 3432 (class 2606 OID 17081)
-- Name: solicitud_reembolso fkbupcwrwdmjlyrudb555s0k64o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solicitud_reembolso
    ADD CONSTRAINT fkbupcwrwdmjlyrudb555s0k64o FOREIGN KEY (cliente_id) REFERENCES public.cliente(id_cliente);


--
-- TOC entry 3428 (class 2606 OID 16956)
-- Name: venta_item fkc9b05e27moa53hgdltgxxnbyv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_item
    ADD CONSTRAINT fkc9b05e27moa53hgdltgxxnbyv FOREIGN KEY (venta_id) REFERENCES public.venta(id);


--
-- TOC entry 3433 (class 2606 OID 17086)
-- Name: solicitud_reembolso fkcw935wkn72q5mt2loths26ng8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.solicitud_reembolso
    ADD CONSTRAINT fkcw935wkn72q5mt2loths26ng8 FOREIGN KEY (procesado_por_id) REFERENCES public.empleado(id_empleado);


--
-- TOC entry 3426 (class 2606 OID 16941)
-- Name: comprobante fkffhbx1kkpuyxd16i2darm8a4a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comprobante
    ADD CONSTRAINT fkffhbx1kkpuyxd16i2darm8a4a FOREIGN KEY (venta_id) REFERENCES public.venta(id);


--
-- TOC entry 3429 (class 2606 OID 16951)
-- Name: venta_item fko3nvrjwr0010ketyo6fe2r0yr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta_item
    ADD CONSTRAINT fko3nvrjwr0010ketyo6fe2r0yr FOREIGN KEY (producto_id) REFERENCES public.producto(id_producto);


--
-- TOC entry 3420 (class 2606 OID 16497)
-- Name: pedido pedido_id_cliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido
    ADD CONSTRAINT pedido_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente) ON DELETE SET NULL;


--
-- TOC entry 3421 (class 2606 OID 16502)
-- Name: pedido_item pedido_item_id_pedido_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido_item
    ADD CONSTRAINT pedido_item_id_pedido_fkey FOREIGN KEY (id_pedido) REFERENCES public.pedido(id_pedido) ON DELETE CASCADE;


--
-- TOC entry 3422 (class 2606 OID 16507)
-- Name: pedido_item pedido_item_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido_item
    ADD CONSTRAINT pedido_item_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);


--
-- TOC entry 3419 (class 2606 OID 16512)
-- Name: producto producto_id_proveedor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_id_proveedor_fkey FOREIGN KEY (id_proveedor) REFERENCES public.proveedor(id_proveedor) ON DELETE SET NULL;


-- Completed on 2025-12-06 03:18:21 -05

--
-- PostgreSQL database dump complete
--

\unrestrict z664sdCTdDYDpspkMBmmLglsHIrDtOitdXIglW0NdLIMbOtzfaj6b3CyKzpy8Vi

