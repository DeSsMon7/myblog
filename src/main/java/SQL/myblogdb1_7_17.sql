/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Timmy
 * Created: May 21, 2017
 */


CREATE TABLE posts (
    id integer NOT NULL,
    title character varying NOT NULL,
    content character varying NOT NULL,
    postdate timestamp,
    users_id integer NOT NULL
);


ALTER TABLE posts OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 16701)
-- Name: posts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE posts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE posts_id_seq OWNER TO postgres;

--
-- TOC entry 2126 (class 0 OID 0)
-- Dependencies: 183
-- Name: posts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE posts_id_seq OWNED BY posts.id;


--
-- TOC entry 184 (class 1259 OID 16703)
-- Name: posts_users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE posts_users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE posts_users_id_seq OWNER TO postgres;

--
-- TOC entry 2127 (class 0 OID 0)
-- Dependencies: 184
-- Name: posts_users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE posts_users_id_seq OWNED BY posts.users_id;


--
-- TOC entry 182 (class 1259 OID 16692)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    id integer NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 16690)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- TOC entry 2128 (class 0 OID 0)
-- Dependencies: 181
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- TOC entry 1992 (class 2604 OID 16708)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY posts ALTER COLUMN id SET DEFAULT nextval('posts_id_seq'::regclass);


--
-- TOC entry 1993 (class 2604 OID 16709)
-- Name: users_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY posts ALTER COLUMN users_id SET DEFAULT nextval('posts_users_id_seq'::regclass);


--
-- TOC entry 1991 (class 2604 OID 16695)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- TOC entry 2117 (class 0 OID 16705)
-- Dependencies: 185
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- TOC entry 2129 (class 0 OID 0)
-- Dependencies: 183
-- Name: posts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('posts_id_seq', 3, true);


--
-- TOC entry 2130 (class 0 OID 0)
-- Dependencies: 184
-- Name: posts_users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('posts_users_id_seq', 1, false);


--
-- TOC entry 2114 (class 0 OID 16692)
-- Dependencies: 182
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- TOC entry 2131 (class 0 OID 0)
-- Dependencies: 181
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_id_seq', 3, true);


--
-- TOC entry 1995 (class 2606 OID 16700)
-- Name: pk_users_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT pk_users_id PRIMARY KEY (id);


--
-- TOC entry 1997 (class 2606 OID 16714)
-- Name: posts_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT posts_id PRIMARY KEY (id);


--
-- TOC entry 1998 (class 2606 OID 16715)
-- Name: fk_posts_users_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT fk_posts_users_id FOREIGN KEY (users_id) REFERENCES users(id);

--
-- New Table "Comment" 29.5.2017
-- Name Comment; Type:Table ; Schema:public; Owner: postgres
--
-- CREATE TABLE public.comment
-- (
--    id serial NOT NULL, 
--    comment_content character varying(255) NOT NULL, 
--    comment_date timestamp without time zone NOT NULL, 
--    user_id serial NOT NULL, 
--    post_id serial NOT NULL, 
--    CONSTRAINT pk_comment_id PRIMARY KEY (id), 
--    CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES public.users (id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
--    CONSTRAINT fk_comment_post_id FOREIGN KEY (post_id) REFERENCES public.posts (id) ON UPDATE NO ACTION ON DELETE NO ACTION
-- ) 
-- WITH (
--   OIDS = FALSE
-- )
-- ;
-- ALTER TABLE public.comment
--   OWNER TO postgres;

--
-- Drop Table "Comment" 01.07.2017
   DROP TABLE public.comment;