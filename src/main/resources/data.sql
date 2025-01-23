-- Създаване на default компания
INSERT INTO companies (name, address, phone_number, email, location_type)
VALUES ('Default Company', 'Default Address', '0888777555', 'default@company.com', 'МЕСТЕН');

-- Свързване на Default Company с всички индустрии
INSERT INTO company_industry (company_id, industry_id)
SELECT 1, id FROM industries;

-- Създаване на индустрии и свързване с Default Company
INSERT INTO industries (name) VALUES ('КУХНЯ');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('БАР');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('АРТИСТИ');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('КОЗМЕТИКА');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('ФОТОГРАФИ');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('ТРАНСПОРТ');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('ИНТЕРНЕТ');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('ХАМАЛИ');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('IT');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('ТЕХНИКА');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());

INSERT INTO industries (name) VALUES ('ДЕНТАЛНА ГРИЖА');
INSERT INTO company_industry (company_id, industry_id) VALUES (1, LAST_INSERT_ID());