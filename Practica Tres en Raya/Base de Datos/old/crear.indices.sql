--
-- TOC entry 1925 (class 2606 OID 41335)
-- Name: activaciones_aid_pkey; Type: CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY activaciones
    ADD CONSTRAINT activaciones_aid_pkey PRIMARY KEY (aid);


--
-- TOC entry 1929 (class 2606 OID 41325)
-- Name: recordarme_pkey; Type: CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY recordarme
    ADD CONSTRAINT recordarme_pkey PRIMARY KEY (id);


--
-- TOC entry 1931 (class 2606 OID 41570)
-- Name: usuarios_conectados_nombre_ukey; Type: CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY usuarios_conectados
    ADD CONSTRAINT usuarios_conectados_nombre_ukey UNIQUE (nombre);


--
-- TOC entry 1919 (class 2606 OID 32981)
-- Name: usuarios_correo_key; Type: CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT usuarios_correo_key UNIQUE (correo);


--
-- TOC entry 1921 (class 2606 OID 32979)
-- Name: usuarios_nombre_key; Type: CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT usuarios_nombre_key UNIQUE (nombre);


--
-- TOC entry 1923 (class 2606 OID 41327)
-- Name: usuarios_uid_pkey; Type: CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT usuarios_uid_pkey PRIMARY KEY (uid);


--
-- TOC entry 1926 (class 1259 OID 41341)
-- Name: fki_activaciones_uid_fkey; Type: INDEX; Schema: tresenraya; Owner: -
--

CREATE INDEX fki_activaciones_uid_fkey ON activaciones USING btree (uid);


--
-- TOC entry 1927 (class 1259 OID 41333)
-- Name: fki_recordarme_uid_fkey; Type: INDEX; Schema: tresenraya; Owner: -
--

CREATE INDEX fki_recordarme_uid_fkey ON recordarme USING btree (uid);


--
-- TOC entry 1932 (class 2606 OID 41336)
-- Name: activaciones_uid_fkey; Type: FK CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY activaciones
    ADD CONSTRAINT activaciones_uid_fkey FOREIGN KEY (uid) REFERENCES usuarios(uid);


--
-- TOC entry 1933 (class 2606 OID 41328)
-- Name: recordarme_uid_fkey; Type: FK CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY recordarme
    ADD CONSTRAINT recordarme_uid_fkey FOREIGN KEY (uid) REFERENCES usuarios(uid);


--
-- TOC entry 1934 (class 2606 OID 41564)
-- Name: usuarios_conectados_uid_fkey; Type: FK CONSTRAINT; Schema: tresenraya; Owner: -
--

ALTER TABLE ONLY usuarios_conectados
    ADD CONSTRAINT usuarios_conectados_uid_fkey FOREIGN KEY (uid) REFERENCES usuarios(uid);