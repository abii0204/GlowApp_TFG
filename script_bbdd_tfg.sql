-- Crear base de datos
CREATE DATABASE tfg
    WITH OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

-- Tabla de usuarios
CREATE TABLE public.usuarios (
                                 id                  SERIAL PRIMARY KEY,
                                 nombre              VARCHAR(100) NOT NULL,
                                 email               VARCHAR(255) NOT NULL UNIQUE,
                                 contraseña_hash     TEXT NOT NULL,
                                 tipo_piel           VARCHAR(50),
                                 codigo_verificacion VARCHAR(6),
                                 verificado          BOOLEAN DEFAULT FALSE
);

-- Tabla de reseñas de productos
CREATE TABLE public.reseñas (
                                id         SERIAL PRIMARY KEY,
                                usuario_id INTEGER REFERENCES public.usuarios ON DELETE CASCADE,
                                producto   VARCHAR(255) NOT NULL,
                                marca      VARCHAR(255) NOT NULL,
                                tipo_piel  VARCHAR(50),
                                comentario TEXT,
                                puntuacion INTEGER CHECK (puntuacion >= 1 AND puntuacion <= 5),
                                fecha      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de seguimiento diario
CREATE TABLE public.seguimiento (
                                    id         SERIAL PRIMARY KEY,
                                    usuario_id INTEGER REFERENCES public.usuarios ON DELETE CASCADE,
                                    fecha      DATE NOT NULL,
                                    selfie     BYTEA,
                                    CONSTRAINT unique_user_date UNIQUE (usuario_id, fecha)
);

-- Tabla de productos usados por día
CREATE TABLE public.productos_usados (
                                         id             SERIAL PRIMARY KEY,
                                         seguimiento_id INTEGER REFERENCES public.seguimiento ON DELETE CASCADE,
                                         producto       TEXT NOT NULL
);

-- Tabla de alimentos registrados por día
CREATE TABLE public.alimentacion (
                                     id             SERIAL PRIMARY KEY,
                                     seguimiento_id INTEGER REFERENCES public.seguimiento ON DELETE CASCADE,
                                     alimento       TEXT NOT NULL
);

-- Tabla de agua consumida por día
CREATE TABLE public.agua_consumida (
                                       id             SERIAL PRIMARY KEY,
                                       seguimiento_id INTEGER REFERENCES public.seguimiento ON DELETE CASCADE,
                                       cantidad_ml    INTEGER NOT NULL
);

-- Tabla de sentimientos disponibles
CREATE TABLE public.sentimientos (
                                     id     SERIAL PRIMARY KEY,
                                     nombre TEXT NOT NULL UNIQUE
);

-- Relación entre sentimientos y seguimiento
CREATE TABLE public.sentimientos_seguimiento (
                                                 id             SERIAL PRIMARY KEY,
                                                 id_seguimiento INTEGER REFERENCES public.seguimiento ON DELETE CASCADE,
                                                 id_sentimiento INTEGER REFERENCES public.sentimientos ON DELETE CASCADE
);

-- Tabla para definir opciones de cantidades de agua
CREATE TABLE public.cantidades_agua (
                                        id          SERIAL PRIMARY KEY,
                                        cantidad_ml INTEGER NOT NULL UNIQUE
);

-- Tabla de artículos informativos
CREATE TABLE public.informacion (
                                    id        SERIAL PRIMARY KEY,
                                    titulo    VARCHAR(255) NOT NULL,
                                    subtitulo VARCHAR(255) NOT NULL,
                                    contenido TEXT NOT NULL,
                                    categoria VARCHAR(100)
);

-- Función y trigger para generar código de verificación y notificar por canal
CREATE FUNCTION public.generar_codigo_verificacion()
    RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
DECLARE
    codigo VARCHAR(6);
BEGIN
    -- Genera un código de 6 dígitos aleatorio
    codigo := LPAD(FLOOR(RANDOM() * 1000000)::TEXT, 6, '0');

    -- Asigna el código al nuevo usuario
    NEW.codigo_verificacion := codigo;

    -- Envía la notificación al canal
    PERFORM pg_notify('registro_notificaciones', json_build_object(
            'id', NEW.id,
            'nombre', NEW.nombre,
            'correo', NEW.email,
            'codigo', NEW.codigo_verificacion
                                                 )::TEXT);

    RETURN NEW;
END;
$$;

-- Trigger que llama a la función anterior
CREATE TRIGGER trigger_generar_codigo
    BEFORE INSERT ON public.usuarios
    FOR EACH ROW
EXECUTE FUNCTION public.generar_codigo_verificacion();



--INSERTS PARA ALGUNAS TABLAS

INSERT INTO public.sentimientos (descripcion) VALUES
                                                  ('Radiante'),
                                                  ('Grasa'),
                                                  ('Seca'),
                                                  ('Irritada'),
                                                  ('Normal'),
                                                  ('Tensa'),
                                                  ('Suave'),
                                                  ('Hidratada'),
                                                  ('Deshidratada'),
                                                  ('Sensible'),
                                                  ('Con picazón'),
                                                  ('Hinchada'),
                                                  ('Opaca'),
                                                  ('Con ardor'),
                                                  ('Escamosa'),
                                                  ('Rugosa'),
                                                  ('Con poros visibles'),
                                                  ('Con tirantez'),
                                                  ('Aliviada'),
                                                  ('Con molestias'),
                                                  ('Con rojeces'),
                                                  ('Lisa'),
                                                  ('Mate'),
                                                  ('Seca en zonas'),
                                                  ('Grasienta en zonas'),
                                                  ('Tirante');



INSERT INTO public.usuarios (nombre, correo, contraseña, tipo_piel) VALUES
                                                                        ('Laura Pérez', 'laura.perez@email.com', 'clave123', 'Normal'),
                                                                        ('Carlos Gómez', 'carlos.gomez@email.com', 'segura456', 'Grasa'),
                                                                        ('Ana Torres', 'ana.torres@email.com', 'password789', 'Seca'),
                                                                        ('Javier Ruiz', 'javier.ruiz@email.com', 'clave000', 'Mixta'),
                                                                        ('María López', 'maria.lopez@email.com', 'abc123', 'Sensible');


INSERT INTO public.resenas (usuario_id, producto, marca, tipo_piel, comentario, puntuacion, fecha) VALUES
                                                                                                       (1, 'Crema Hidratante Light', 'GlowCare', 'Normal', 'Me encantó la textura y cómo deja mi piel.', 5, CURRENT_DATE),
                                                                                                       (2, 'Limpiador en gel purificante', 'SkinFix', 'Grasa', 'Controla bien la grasa, pero reseca un poco.', 4, CURRENT_DATE),
                                                                                                       (3, 'Crema nutritiva noche', 'DermaPlus', 'Seca', 'Muy hidratante, mi piel se siente mejor cada mañana.', 5, CURRENT_DATE),
                                                                                                       (4, 'Mascarilla combinada', 'BalanceSkin', 'Mixta', 'Buena para zonas grasas y secas, cumple bien.', 4, CURRENT_DATE),
                                                                                                       (5, 'Protector solar mineral', 'SafeSun', 'Sensible', 'No me causa irritaciones, lo recomiendo.', 5, CURRENT_DATE),
                                                                                                       (1, 'Tónico facial natural', 'BioTone', 'Normal', 'Refrescante y suave, ideal para cada día.', 4, CURRENT_DATE),
                                                                                                       (2, 'Mascarilla de arcilla', 'PureSkin', 'Grasa', 'Ayuda a reducir granitos y poros, muy eficaz.', 5, CURRENT_DATE),
                                                                                                       (3, 'Aceite facial', 'HydraEssence', 'Seca', 'Me dejó la piel más elástica y luminosa.', 4, CURRENT_DATE),
                                                                                                       (4, 'Serum hidratante', 'AquaDerm', 'Mixta', 'Absorción rápida y sin sensación grasa.', 5, CURRENT_DATE),
                                                                                                       (5, 'Agua micelar calmante', 'DelicateCare', 'Sensible', 'Limpia muy bien sin irritar nada.', 5, CURRENT_DATE);


-- Sección 1: Rutina básica
INSERT INTO public.informacion (titulo, subtitulo, contenido, categoria) VALUES
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 1: Limpieza', 'Usa un limpiador suave que no altere el equilibrio natural de tu piel.', 'Normal'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 2: Tonificación', 'Aplica un tónico hidratante para mantener el pH equilibrado.', 'Normal'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 3: Hidratación', 'Utiliza una crema hidratante ligera en la mañana y más nutritiva por la noche.', 'Normal'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 4: Protección solar', 'No olvides usar protector solar todos los días.', 'Normal'),

-- Sección 2: Ingredientes que deberías buscar
                                                                             ('Ingredientes que deberías buscar', 'Ácido hialurónico', 'Ayuda a mantener la piel hidratada y tersa.', 'Normal'),
                                                                             ('Ingredientes que deberías buscar', 'Glicerina', 'Proporciona hidratación sin engrasar.', 'Normal'),

-- Sección 3: Errores comunes que debes evitar
                                                                             ('Errores comunes que debes evitar', 'Uso excesivo de exfoliantes', 'Puede eliminar aceites naturales importantes.', 'Normal'),
                                                                             ('Errores comunes que debes evitar', 'No retirar bien el maquillaje', 'Obstruye poros y provoca imperfecciones.', 'Normal'),

-- Sección 4: Consejos de estilo de vida
                                                                             ('Consejos de estilo de vida', 'Hidratación constante', 'Beber agua ayuda a mantener el equilibrio de la piel.', 'Normal'),
                                                                             ('Consejos de estilo de vida', 'Dormir bien', 'Un buen descanso mejora la renovación celular.', 'Normal'),

-- Sección 5: Mascarillas recomendadas
                                                                             ('Mascarillas recomendadas', 'Mascarilla de miel y avena', 'Hidrata y suaviza la piel sin irritarla.', 'Normal'),
                                                                             ('Mascarillas recomendadas', 'Mascarilla de yogur natural', 'Refresca la piel y la deja luminosa.', 'Normal');
-- Sección 1: Rutina básica
INSERT INTO public.informacion (titulo, subtitulo, contenido, categoria) VALUES
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 1: Limpieza', 'Usa un limpiador en gel o espuma que controle el exceso de grasa.', 'Grasa'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 2: Tonificación', 'Elige tónicos con ácido salicílico o hamamelis.', 'Grasa'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 3: Hidratación', 'Opta por hidratantes oil-free y no comedogénicos.', 'Grasa'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 4: Protección solar', 'Usa protector solar libre de aceites y de textura ligera.', 'Grasa'),

-- Sección 2: Ingredientes que deberías buscar
                                                                             ('Ingredientes que deberías buscar', 'Ácido salicílico', 'Ayuda a controlar el sebo y prevenir brotes.', 'Grasa'),
                                                                             ('Ingredientes que deberías buscar', 'Niacinamida', 'Reduce el brillo y mejora la textura de la piel.', 'Grasa'),

-- Sección 3: Errores comunes que debes evitar
                                                                             ('Errores comunes que debes evitar', 'Lavarse el rostro con demasiada frecuencia', 'Esto estimula más producción de grasa.', 'Grasa'),
                                                                             ('Errores comunes que debes evitar', 'No hidratar la piel', 'Incluso la piel grasa necesita hidratación adecuada.', 'Grasa'),

-- Sección 4: Consejos de estilo de vida
                                                                             ('Consejos de estilo de vida', 'Evitar alimentos muy grasos o azucarados', 'Estos pueden influir en la producción de sebo.', 'Grasa'),
                                                                             ('Consejos de estilo de vida', 'Usar fundas de almohada limpias', 'Acumulan bacterias que pueden generar brotes.', 'Grasa'),

-- Sección 5: Mascarillas recomendadas
                                                                             ('Mascarillas recomendadas', 'Mascarilla de arcilla verde', 'Ideal para absorber el exceso de grasa y purificar la piel.', 'Grasa'),
                                                                             ('Mascarillas recomendadas', 'Mascarilla de carbón activado', 'Desintoxica la piel y ayuda a controlar los poros.', 'Grasa');
-- Sección 1: Rutina básica
INSERT INTO public.informacion (titulo, subtitulo, contenido, categoria) VALUES
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 1: Limpieza', 'Usa un limpiador cremoso o en aceite que no reseque.', 'Seca'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 2: Tonificación', 'Aplica un tónico sin alcohol con ingredientes humectantes.', 'Seca'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 3: Hidratación', 'Usa cremas ricas en lípidos y ceramidas, especialmente por la noche.', 'Seca'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 4: Protección solar', 'Utiliza un protector solar hidratante.', 'Seca'),

-- Sección 2: Ingredientes que deberías buscar
                                                                             ('Ingredientes que deberías buscar', 'Manteca de karité', 'Nutre profundamente la piel seca.', 'Seca'),
                                                                             ('Ingredientes que deberías buscar', 'Ácido hialurónico', 'Aporta hidratación duradera en distintas capas de la piel.', 'Seca'),

-- Sección 3: Errores comunes que debes evitar
                                                                             ('Errores comunes que debes evitar', 'Duchas muy calientes y prolongadas', 'Pueden eliminar la barrera natural de la piel.', 'Seca'),
                                                                             ('Errores comunes que debes evitar', 'No usar productos selladores de hidratación', 'La piel seca necesita mantener la humedad.', 'Seca'),

-- Sección 4: Consejos de estilo de vida
                                                                             ('Consejos de estilo de vida', 'Uso de humidificadores', 'Ayudan a combatir la sequedad ambiental.', 'Seca'),
                                                                             ('Consejos de estilo de vida', 'Beber suficiente agua', 'La hidratación interna también se refleja en la piel.', 'Seca'),

-- Sección 5: Mascarillas recomendadas
                                                                             ('Mascarillas recomendadas', 'Mascarilla de aguacate y miel', 'Ideal para nutrir y suavizar la piel seca.', 'Seca'),
                                                                             ('Mascarillas recomendadas', 'Mascarilla de aceite de coco', 'Aporta nutrición intensa en pieles muy secas.', 'Seca');
-- Sección 1: Rutina básica
INSERT INTO public.informacion (titulo, subtitulo, contenido, categoria) VALUES
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 1: Limpieza', 'Usa un limpiador equilibrado que no reseque ni engrase.', 'Mixta'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 2: Tonificación', 'Elige tónicos sin alcohol que refresquen la piel.', 'Mixta'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 3: Hidratación', 'Hidrata con productos ligeros, pero aplica cremas más nutritivas en zonas secas.', 'Mixta'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 4: Protección solar', 'Utiliza protección solar para todo tipo de pieles, preferiblemente fluida.', 'Mixta'),

-- Sección 2: Ingredientes que deberías buscar
                                                                             ('Ingredientes que deberías buscar', 'Aloe vera', 'Hidrata sin dejar sensación grasosa.', 'Mixta'),
                                                                             ('Ingredientes que deberías buscar', 'Ácido hialurónico + niacinamida', 'Combinación ideal para balancear zonas secas y grasas.', 'Mixta'),

-- Sección 3: Errores comunes que debes evitar
                                                                             ('Errores comunes que debes evitar', 'No tratar las zonas por separado', 'Cada zona necesita atención específica.', 'Mixta'),
                                                                             ('Errores comunes que debes evitar', 'Usar productos muy agresivos', 'Pueden resecar las mejillas o estimular más grasa en la zona T.', 'Mixta'),

-- Sección 4: Consejos de estilo de vida
                                                                             ('Consejos de estilo de vida', 'Dieta equilibrada', 'Reduce el azúcar y grasas para mantener la piel estable.', 'Mixta'),
                                                                             ('Consejos de estilo de vida', 'Evitar cambios bruscos de temperatura', 'Pueden alterar el equilibrio natural de la piel.', 'Mixta'),

-- Sección 5: Mascarillas recomendadas
                                                                             ('Mascarillas recomendadas', 'Mascarilla de pepino y yogur', 'Refresca la piel y equilibra su textura.', 'Mixta'),
                                                                             ('Mascarillas recomendadas', 'Mascarilla combinada (arcilla T y crema en mejillas)', 'Personaliza la mascarilla según las necesidades de cada zona.', 'Mixta');
-- Sección 1: Rutina básica
INSERT INTO public.informacion (titulo, subtitulo, contenido, categoria) VALUES
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 1: Limpieza', 'Usa un limpiador hipoalergénico, sin fragancias.', 'Sensible'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 2: Tonificación', 'Evita tónicos con alcohol o perfumes. Usa brumas suaves.', 'Sensible'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 3: Hidratación', 'Hidrata con productos específicos para piel sensible.', 'Sensible'),
                                                                             ('Rutina básica de cuidado de la piel', 'Paso 4: Protección solar', 'Usa protectores solares minerales con óxido de zinc o titanio.', 'Sensible'),

-- Sección 2: Ingredientes que deberías buscar
                                                                             ('Ingredientes que deberías buscar', 'Avena coloidal', 'Calma y alivia irritaciones.', 'Sensible'),
                                                                             ('Ingredientes que deberías buscar', 'Centella asiática', 'Regenera y refuerza la barrera cutánea.', 'Sensible'),

-- Sección 3: Errores comunes que debes evitar
                                                                             ('Errores comunes que debes evitar', 'Probar demasiados productos a la vez', 'Puede desencadenar reacciones adversas.', 'Sensible'),
                                                                             ('Errores comunes que debes evitar', 'No leer los ingredientes', 'Evita fragancias, alcoholes y colorantes.', 'Sensible'),

-- Sección 4: Consejos de estilo de vida
                                                                             ('Consejos de estilo de vida', 'Ambientes con aire limpio', 'Evita contaminación, humo o polvo excesivo.', 'Sensible'),
                                                                             ('Consejos de estilo de vida', 'Ropa suave', 'Evita tejidos sintéticos o ásperos en contacto con el rostro.', 'Sensible'),

-- Sección 5: Mascarillas recomendadas
                                                                             ('Mascarillas recomendadas', 'Mascarilla de aloe vera pura', 'Hidrata, calma y reduce la inflamación.', 'Sensible'),
                                                                             ('Mascarillas recomendadas', 'Mascarilla de manzanilla y avena', 'Perfecta para aliviar enrojecimientos y escozor.', 'Sensible');
