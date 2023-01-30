CREATE TABLE IF NOT EXISTS tree_species
(
    genus        VARCHAR(20) NOT NULL,
    species      VARCHAR(20) NOT NULL,
    common_name  VARCHAR(40) NOT NULL,
    species_code VARCHAR(10) NOT NULL,

    PRIMARY KEY (genus, species)
);

CREATE TABLE IF NOT EXISTS tree_benefits
(
    species_code       VARCHAR(10)      NOT NULL,
    diameter           DOUBLE PRECISION NOT NULL,
    aq_nox_avoided     DOUBLE PRECISION NOT NULL,
    aq_nox_dep         DOUBLE PRECISION NOT NULL,
    aq_ozone_dep       DOUBLE PRECISION NOT NULL,
    aq_pm10_avoided    DOUBLE PRECISION NOT NULL,
    aq_pm10_dep        DOUBLE PRECISION NOT NULL,
    aq_sox_avoided     DOUBLE PRECISION NOT NULL,
    aq_sox_dep         DOUBLE PRECISION NOT NULL,
    aq_voc_avoided     DOUBLE PRECISION NOT NULL,
    co2_avoided        DOUBLE PRECISION NOT NULL,
    co2_sequestered    DOUBLE PRECISION NOT NULL,
    co2_storage        DOUBLE PRECISION NOT NULL,
    electricity        DOUBLE PRECISION NOT NULL,
    hydro_interception DOUBLE PRECISION NOT NULL,
    natural_gas        DOUBLE PRECISION NOT NULL,

    PRIMARY KEY (species_code, diameter)
);
