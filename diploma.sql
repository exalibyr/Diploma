ALTER TABLE nano_l_answers ADD FOREIGN KEY (ANSWER_TYPE_ID) REFERENCES nano_l_answers_types(ANSWER_TYPE_ID)
ALTER TABLE nano_l_fill ADD FOREIGN KEY (FILL_CATEGORY) REFERENCES category_fill(id)
ALTER TABLE nano_l_questions ADD FOREIGN KEY (QUESTION_GROUP_ID) REFERENCES nano_l_question_groups(QUESTION_GROUP_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (COMPOSITE_ID) REFERENCES nano_l_composite(id)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (MATRIX_ID) REFERENCES nano_l_matrix(MATRIX_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (FILL_ID) REFERENCES nano_l_fill(FILL_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (ARTICLE_ID) REFERENCES nano_l_articles(ARTICLE_ID)	
ALTER TABLE nano_l_articles ADD FOREIGN KEY (COUNTRY_ID) REFERENCES nano_countries(COUNTRY_ID)
ALTER TABLE nano_l_matrix ADD FOREIGN KEY (MATRIX_CATEGORY) REFERENCES category_matrix(id)
ALTER TABLE `nano_l_composite` ADD `matrix_fraction` TEXT AFTER `full_name`;
ALTER TABLE `nano_l_composite` ADD `fill_fraction` TEXT AFTER `matrix_fraction`;

CREATE VIEW articles AS
SELECT nlc.name, nla.ARTICLE_NAME, nla.YEAR_ID, nla.JOURNAL_NAME, nla.PAGES, nla.AUTHORS, nc.COUNTRY_NAME, nla.FILE
FROM nano_f_datum nfd, nano_l_articles nla, nano_l_composite nlc, nano_countries nc, nano_l_matrix nlm, category_matrix cm
WHERE nla.ARTICLE_ID = nfd.ARTICLE_ID
AND nlc.id = nfd.COMPOSITE_ID
AND nc.COUNTRY_ID = nla.COUNTRY_ID
AND nfd.MATRIX_ID = nlm.MATRIX_ID
AND nlm.MATRIX_CATEGORY = cm.id
AND nfd.DATA_ID <> 367
AND cm.name = "Керамика"

CREATE VIEW properties_view_old AS
SELECT nlc.name, nlm.MATRIX_NAME, nlf.FILL_NAME, nlq.QUESTION_NAME, nfa.ANSWER_TEXT, nla.ANSWER_NAME
FROM nano_l_answers nla, nano_l_questions nlq, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm, nano_l_fill nlf, nano_l_composite nlc
WHERE nlq.QUESTION_ID = nla.QUESTION_ID
AND nla.ANSWER_ID = nfa.ANSWER_ID
AND nla.QUESTION_ID = nfa.QUESTION_ID
AND nfa.DATA_ID = nfd.DATA_ID
AND nlc.id = nfd.COMPOSITE_ID
AND nlm.MATRIX_ID = nfd.MATRIX_ID
AND nlf.FILL_ID = nfd.FILL_ID
AND nfd.DATA_ID = 367                
                    
CREATE VIEW properties_view AS
SELECT nlc.name, nlm.MATRIX_NAME, nlf.FILL_NAME, nlq.QUESTION_NAME, nfa.ANSWER_TEXT, nla.ANSWER_NAME
FROM nano_l_answers nla, nano_l_questions nlq, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm, nano_l_fill nlf, nano_l_composite nlc, category_matrix cm
WHERE nlq.QUESTION_ID = nla.QUESTION_ID
AND nla.ANSWER_ID = nfa.ANSWER_ID
AND nla.QUESTION_ID = nfa.QUESTION_ID
AND nfa.DATA_ID = nfd.DATA_ID
AND nlc.id = nfd.COMPOSITE_ID
AND nlm.MATRIX_ID = nfd.MATRIX_ID
AND nlf.FILL_ID = nfd.FILL_ID
AND nfd.DATA_ID <> 367
AND nlm.MATRIX_CATEGORY = cm.id
AND cm.name = "Керамика"

CREATE VIEW matrix_kinds AS         
SELECT DISTINCT nlm.MATRIX_NAME
FROM nano_l_matrix nlm, category_matrix cm, nano_f_datum nfd
WHERE nlm.MATRIX_CATEGORY = cm.id
AND nfd.MATRIX_ID = nlm.MATRIX_ID
AND nfd.DATA_ID <> 367
AND cm.name = "Керамика"

DELIMITER //
CREATE PROCEDURE update_questions (matrixName varchar(255))                    
BEGIN 
	SELECT DISTINCT nlq.QUESTION_NAME, nlq.QUESTION_NAME_ENG 
	FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm
	WHERE nlq.QUESTION_ID = nla.QUESTION_ID 
    AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
	AND nlm.MATRIX_NAME = matrixName
    AND nlq.QUESTION_GROUP_ID <> 4
    AND nlq.QUESTION_GROUP_ID <> 3
    AND nlq.QUESTION_GROUP_ID <> 15;
END
//

DELIMITER //
CREATE PROCEDURE get_values(matrixName varchar(255), questionName varchar(255))
BEGIN
	SELECT nlf.FILL_NAME, nfa.ANSWER_TEXT, nla.ANSWER_NAME
	FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm, nano_l_fill nlf
	WHERE nlq.QUESTION_ID = nla.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
    AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
    AND nlf.FILL_ID = nfd.FILL_ID
	AND nlm.MATRIX_NAME = matrixName
    AND nlq.QUESTION_NAME = questionName;
END

CREATE VIEW target_properties AS
SELECT nlm.MATRIX_NAME, nlc.name, nlf.FILL_NAME, nlq.QUESTION_NAME, nlq.QUESTION_NAME_ENG, nfa.ANSWER_TEXT, nla.ANSWER_NAME
FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm, nano_l_fill nlf, category_matrix cm, nano_l_composite nlc
WHERE nlq.QUESTION_ID = nla.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
    AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
    AND nlf.FILL_ID = nfd.FILL_ID
    AND cm.id = nlm.MATRIX_CATEGORY
    AND cm.name = 'Керамика'
    AND nfd.DATA_ID <> 367
    AND nlc.id = nfd.COMPOSITE_ID
    
CREATE VIEW application_area AS
SELECT  nlc.name, nlq.QUESTION_NAME, nfa.ANSWER_TEXT
FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm, category_matrix cm, nano_l_composite nlc
WHERE nlq.QUESTION_ID = nla.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
    AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
    AND cm.id = nlm.MATRIX_CATEGORY
    AND cm.name = 'Керамика'
    AND nfd.DATA_ID <> 367
    AND nlc.id = nfd.COMPOSITE_ID
    AND nlq.QUESTION_GROUP_ID = 4
    
CREATE VIEW Synthesis_description AS
SELECT  nlc.name, nlq.QUESTION_NAME, nfa.ANSWER_TEXT
FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm, category_matrix cm, nano_l_composite nlc
WHERE nlq.QUESTION_ID = nla.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
    AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
    AND cm.id = nlm.MATRIX_CATEGORY
    AND cm.name = 'Керамика'
    AND nfd.DATA_ID <> 367
    AND nlc.id = nfd.COMPOSITE_ID
    AND (nlq.QUESTION_GROUP_ID = 3 OR nlq.QUESTION_GROUP_ID = 15)
    
    
INSERT INTO nano_l_answers_types VALUES (6, 'INTERVAL')

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//ЗАПОЛНЕНИЕ БД СТАТЬЯМИ//
___________________________________________________________________________________________________________________________________________________________________________________
INSERT INTO nano_l_articles VALUES 
(355, '4.2.1. Керамика на основе Al2O3', 2004, 'Техническая керамика. Учебное пособие ТПУ Томск.', NULL, 1, '30-36', '6', NULL, 'С.В.Матренин, А.И.Слосман', 7, 'mechanical_engineering.pdf')           
INSERT INTO nano_l_composite VALUES (60, 'Al2O3-TiO2', 'спечённая корундовая керамика')
INSERT INTO nano_l_matrix VALUES (196, 12, 'Al2O3', 'Al2O3', NULL)
INSERT INTO nano_l_fill VALUES (66, 2, 'TiO2', 'оксид титана', 'Titan oxide')
INSERT into nano_f_datum VALUES (414, 355, 60, 196, 66)
INSERT INTO nano_l_questions VALUES (240, 10, 'плотность', 'density', NULL, 1)
INSERT INTO nano_l_answers VALUES (240, 23, 2, 'г/см3', 'g/sm3')
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.96', 414, Null)
INSERT into nano_l_answers VALUES (204, 24, 2, '°C', '°C')
INSERT into nano_f_answers VALUES (204, 24, NULL, '2050', 414, NUll)
INSERT INTO nano_l_answers VALUES 
(53, 25, 2, 'Вт/(K*м); при 100°C; при 400°C; при 1000°C', 'W/(K*m); at 100°C; at 400°C; at 1000°C')
INSERT into nano_f_answers VALUES (53, 25, NULL, '30.14; 12.4; 6.4;', 414, NUll)
INSERT into nano_l_answers VALUES 
(87, 26, 2, 'Ом*см; при 100°C; при 1300°C', 'Ohm*cm; at 100°C; at 1300°C')
INSERT into nano_f_answers VALUES (87, 26, NULL, '30000000000; 0.0009;', 414, NUll)
INSERT INTO nano_l_questions VALUES (241, 9, 'линейный коэффициент теплового расширения', 'Linear thermal expansion coefficient', '', 1)
INSERT into nano_l_answers VALUES (241, 27, 2, 'α•10⁶ K⁻⁶ (20-1400°C)', 'α•10⁶ K⁻⁶ (20-1400°C)')
INSERT into nano_f_answers VALUES (241, 27, NULL, '8', 414, NUll)
INSERT into nano_l_answers VALUES (71, 28, 2, 'ГПа; при 20°C; при 1000°C; при 1500°C', 'GPa; at 20°C; at 1000°C; 1500°C')
INSERT into nano_f_answers VALUES (71, 28, NULL, '374; 315; 147;', 414, NUll)
INSERT INTO nano_l_questions VALUES (242, 10, 'предел прочности при изгибе', 'ultimate bending strength', '', 1)
INSERT into nano_l_answers VALUES (242, 29, 2, 'МПА; 20°C; 1500°C', 'MPa; 20°C; 1500°C')
INSERT into nano_f_answers VALUES (242, 29, NULL, '650; 50', 414, NUll)
INSERT INTO nano_l_questions VALUES (243, 10, 'микротвёрдость', 'microhardness', '', 1)
INSERT into nano_l_answers VALUES (243, 30, 2, 'ГПа (при 20°C)', 'GPa (at 20°C)')
INSERT into nano_f_answers VALUES (243, 30, NULL, '26', 414, NUll)
__________________________________________________________________________________________________________________________________________________________________________________________

INSERT INTO nano_l_articles VALUES 
(356, 'Разработка бесспековой технологии вакуумплотной корундовой керамики группы вк100 для нужд электронной техники', 2016, 'Разработка бесспековой технологии вакуумплотной корундовой керамики группы вк100 для нужд электронной техники: дисс., Москва', NULL, 1, '', '', NULL, 'Амелина О.Д.', 7, '')      
INSERT INTO nano_l_composite VALUES (61, 'ВК 100-1', 'поликор')
INSERT INTO nano_l_fill VALUES (67, 2, 'MgO', 'оксид магния', 'MgO')
INSERT into nano_f_datum VALUES (415, 356, 61, 196, 67)
INSERT INTO nano_l_answers VALUES (123, 3, 6, '%', '%')//процент наполнителя
INSERT INTO nano_f_answers VALUES (123, 3, NULL, '0.1-0.2', 415, NULL)
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.96', 415, Null)//плтность
INSERT INTO nano_l_answers VALUES (63, 30, 2, '%', '%')//водопоглощение
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 415, Null)
INSERT into nano_l_answers VALUES (242, 31, 2, 'МПА', 'MPa')//предел прочности при изгибе
INSERT into nano_f_answers VALUES (242, 31, NULL, '280', 415, NUll)
INSERT into nano_l_answers VALUES (71, 32, 2, 'Е•10⁻², ГПа', 'Е•10⁻², GPa')//модуль упругости
INSERT into nano_f_answers VALUES (71, 32, NULL, '3.5', 415, NUll)
INSERT into nano_l_answers VALUES (85, 34, 2, 'при 25°C и частоте 10⁶ Гц', 'at 25°C and frequency 10⁶ Hz')/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_f_answers VALUES (85, 34, NULL, '10.3', 415, NUll)
INSERT into nano_l_answers VALUES (86, 8, 2, 'tgδ•10⁴, при 25°C и частоте 10⁶ Гц', 'tgδ•10⁴, at 25°C and frecuency 10⁶ Hz');/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (86, 8, NULL, '2', 415, NUll);
INSERT into nano_l_answers VALUES (86, 9, 2, 'tgδ•10⁴, при 25°C и частоте 10¹⁰ Гц', 'tgδ•10⁴, at 25°C and frecuency 10¹⁰ Hz');/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10¹⁰Гц*/
INSERT into nano_f_answers VALUES (86, 9, NULL, '1', 415, NUll);
INSERT into nano_l_answers VALUES (87, 36, 2, ', Ом•см, при 100°С', 'Ohm•cm, at 100°С')//Удельное электрическое сопротивление
INSERT into nano_f_answers VALUES (87, 36, NULL, '100000000000000', 415, NUll)
INSERT into nano_l_answers VALUES (241, 1, 2, 'α•10⁶ при 20-200°C, K⁻¹', 'α•10⁶ at 20-200°C, K⁻¹');//линейный коэффициент теплового расширения
INSERT into nano_l_answers VALUES (241, 2, 2, 'α•10⁶ при 20-500°C, K⁻¹', 'α•10⁶ at 20-500°C, K⁻¹');
INSERT into nano_l_answers VALUES (241, 3, 2, 'α•10⁶ при 20-1000°C, K⁻¹', 'α•10⁶ at 20-1000°C, K⁻¹');
INSERT into nano_l_answers VALUES (241, 37, 2, 'α•10⁶ при 20-900°C, K⁻¹', 'α•10⁶ at 20-900°C, K⁻¹');

__________________________________________________________________________________________________________________________________________________________________________________________

INSERT INTO nano_l_articles VALUES 
(357, 'Вакуум-плотная корундовая керамика на основе ультрадисперсных порошков', 2010, 'НАНОИНДУСТРИЯ', NULL, 1, '40-41', '2', '5', 'Амелина O.Д., Нестеров C.', 7, 'article_1812_291.pdf') 

INSERT INTO nano_l_composite VALUES (62, 'ВК 100-2', 'КМ')
INSERT into nano_f_datum VALUES (416, 357, 62, 196, 67)
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.88', 416, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 416, Null);/*водопоглощение*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '320', 416, NUll);/*предел прочности при изгибе*/
INSERT INTO nano_l_questions VALUES (244, 10, 'Ударная прочность', 'impact strength', '', 1);/*Ударная прочность,кПа•м2*/
INSERT into nano_f_answers VALUES (244, 1, NULL, '5', 416, NUll);
INSERT into nano_l_answers VALUES (244, 1, 2, 'кПа•м²', 'kPa•m²');
INSERT into nano_f_answers VALUES (71, 32, NULL, '3.9', 416, NUll);/*модуль упругости*/
INSERT INTO nano_l_answers VALUES (53, 3, 2, 'Вт/(К•м)', 'W/(K•m)');/*Коэффициент теплопроводности*/
INSERT into nano_f_answers VALUES (53, 3, NULL, '42', 416, NUll);
INSERT into nano_f_answers VALUES (85, 34, NULL, '10.5', 416, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_l_answers VALUES (85, 35, 2, 'при 25°C и частоте 10¹⁰ Гц', 'at 25°C and frequency 10¹⁰ Hz');/*Диэлектрическая проницаемость при 25°C и частоте 10¹⁰ Гц*/
INSERT into nano_f_answers VALUES (85, 35, NULL, '10.1', 416, NUll);
INSERT into nano_f_answers VALUES (86, 8, NULL, '2', 416, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (86, 9, NULL, '1', 416, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10¹⁰Гц*/
INSERT into nano_f_answers VALUES (87, 36, NULL, '100000000000000', 416, NUll);/*Удельное электрическое сопротивление*/
INSERT into nano_l_answers VALUES (88, 7, 2, 'кВ/мм', 'kV/mm');/*Электрическая прочность, кВ/мм*/
INSERT into nano_f_answers VALUES (88, 7, NULL, '42', 416, NUll);
INSERT into nano_f_answers VALUES (241, 37, NULL, '7.9', 416, NUll);/*линейный коэффициент теплового расширения*/
INSERT into nano_l_answers VALUES (25, 4, 3, 'название', 'name');/*области применения композита*/
INSERT into nano_f_answers VALUES (25, 4, NULL, 'авиационно-космическая	и ракетная техника', 416, NUll);
INSERT into nano_l_answers VALUES (25, 5, 3, 'название', 'name');
INSERT into nano_f_answers VALUES (25, 5, NULL, 'выходные устройства мощных СВЧ-приборов', 416, NUll);
INSERT into nano_l_answers VALUES (25, 6, 3, 'название', 'name');
INSERT into nano_f_answers VALUES (25, 6, NULL, 'монолитные интегральные схемы усилителей большой мощности', 416, NUll);
INSERT into nano_l_answers VALUES (25, 7, 3, 'название', 'name');
INSERT into nano_f_answers VALUES (25, 7, NULL, 'системы охлаждения термоэлектрических преобразователей на основе элементов Пельтье', 416, NUll);
INSERT into nano_l_answers VALUES (25, 8, 3, 'название', 'name');
INSERT into nano_f_answers VALUES (25, 8, NULL, 'теплопроводящие изоляторы нагревателей активных термостатов', 416, NUll);
INSERT into nano_l_answers VALUES (25, 9, 3, 'название', 'name');
INSERT into nano_f_answers VALUES (25, 9, NULL, 'сборки линеек лазерных диодов', 416, NUll);

INSERT INTO nano_l_composite VALUES (63, 'ВК 94-1', '22ХС');
INSERT into nano_f_datum VALUES (417, 357, 63, 196, 67);
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.65', 417, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 417, Null);/*водопоглощение*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '320', 417, NUll);/*предел прочности при изгибе*/
INSERT into nano_f_answers VALUES (85, 34, NULL, '10.3', 417, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_f_answers VALUES (85, 35, NULL, '10.3', 417, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10¹⁰ Гц*/
INSERT into nano_f_answers VALUES (86, 8, NULL, '6', 417, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (86, 9, NULL, '15', 417, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10¹⁰Гц*/
INSERT into nano_f_answers VALUES (87, 36, NULL, '10000000000000', 417, NUll);/*Удельное электрическое сопротивление*/
INSERT into nano_f_answers VALUES (25, 4, NULL, 'авиационно-космическая	и ракетная техника', 417, NUll);/*области применения композита*/
INSERT into nano_f_answers VALUES (25, 5, NULL, 'выходные устройства мощных СВЧ-приборов', 417, NUll);
INSERT into nano_f_answers VALUES (25, 6, NULL, 'монолитные интегральные схемы усилителей большой мощности', 417, NUll);
INSERT into nano_f_answers VALUES (25, 7, NULL, 'системы охлаждения термоэлектрических преобразователей на основе элементов Пельтье', 417, NUll);
INSERT into nano_f_answers VALUES (25, 8, NULL, 'теплопроводящие изоляторы нагревателей активных термостатов', 417, NUll);
INSERT into nano_f_answers VALUES (25, 9, NULL, 'сборки линеек лазерных диодов', 417, NUll);

__________________________________________________________________________________________________________________________________________________________________________________________

INSERT INTO nano_l_articles VALUES 
(358, '[Электронный ресурс] / Материал URL: http://www.vaccer.ru/material', 2006, 'OOO «Вакуумная керамика»', NULL, 1, '', '1', NULL, 'OOO «Вакуумная керамика»', 7, '') 

INSERT INTO nano_l_composite VALUES (64, 'ВК 98-1', 'сапфирит-16');
INSERT into nano_f_datum VALUES (418, 358, 64, 196, 67);
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.88', 418, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 418, Null);/*водопоглощение*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '300', 418, NUll);/*предел прочности при изгибе*/
INSERT into nano_f_answers VALUES (85, 34, NULL, '10.8', 418, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_f_answers VALUES (85, 35, NULL, '10.3', 418, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10¹⁰ Гц*/
INSERT into nano_f_answers VALUES (86, 8, NULL, '2', 418, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (86, 9, NULL, '1', 418, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10¹⁰Гц*/
INSERT into nano_f_answers VALUES (87, 36, NULL, '100000000000000', 418, NUll);/*Удельное электрическое сопротивление*/
INSERT into nano_f_answers VALUES (241, 37, NULL, '8.2', 418, NUll);/*линейный коэффициент теплового расширения*/
INSERT into nano_f_answers VALUES (241, 1, NULL, '6.3', 418, NUll);
INSERT into nano_f_answers VALUES (241, 2, NULL, '7.5', 418, NUll);

INSERT INTO nano_l_composite VALUES (65, 'ВК 94-2', 'М-7');
INSERT into nano_f_datum VALUES (419, 358, 65, 196, 67);
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.6', 419, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 419, Null);/*водопоглощение*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '300', 419, NUll);/*предел прочности при изгибе*/
INSERT into nano_f_answers VALUES (85, 34, NULL, '9.5', 419, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_f_answers VALUES (85, 35, NULL, '8.6', 419, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10¹⁰ Гц*/
INSERT into nano_f_answers VALUES (86, 8, NULL, '6', 419, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (86, 9, NULL, '9', 419, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10¹⁰Гц*/
INSERT into nano_f_answers VALUES (87, 36, NULL, '10000000000000', 419, NUll);/*Удельное электрическое сопротивление*/
INSERT into nano_f_answers VALUES (241, 37, NULL, '7.4', 419, NUll);/*линейный коэффициент теплового расширения*/
INSERT into nano_f_answers VALUES (241, 1, NULL, '5.7', 419, NUll);
INSERT into nano_f_answers VALUES (241, 2, NULL, '6.5', 419, NUll);

INSERT INTO nano_l_composite VALUES (66, 'ВК 95-1', 'ВГ-1У');
INSERT into nano_f_datum VALUES (420, 358, 66, 196, 67);
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.67', 420, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 420, Null);/*водопоглощение*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '310', 420, NUll);/*предел прочности при изгибе*/
INSERT into nano_f_answers VALUES (85, 34, NULL, '10', 420, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_f_answers VALUES (85, 35, NULL, '10.2', 420, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10¹⁰ Гц*/
INSERT into nano_f_answers VALUES (86, 8, NULL, '5', 420, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (86, 9, NULL, '10', 420, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10¹⁰Гц*/
INSERT into nano_f_answers VALUES (87, 36, NULL, '10000000000000', 420, NUll);/*Удельное электрическое сопротивление*/
INSERT into nano_f_answers VALUES (241, 37, NULL, '7.9', 420, NUll);/*линейный коэффициент теплового расширения*/
INSERT into nano_f_answers VALUES (241, 1, NULL, '5.8', 420, NUll);
INSERT into nano_f_answers VALUES (241, 2, NULL, '6.7', 420, NUll);

INSERT INTO nano_l_composite VALUES (67, 'ГР.795', 'ГБ-7');
INSERT into nano_f_datum VALUES (421, 358, 67, 196, 67);
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.8', 421, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 421, Null);/*водопоглощение*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '300', 421, NUll);/*предел прочности при изгибе*/
INSERT into nano_f_answers VALUES (85, 34, NULL, '9', 421, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_f_answers VALUES (86, 8, NULL, '5', 421, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (87, 36, NULL, '100000000000000', 421, NUll);/*Удельное электрическое сопротивление*/
INSERT into nano_f_answers VALUES (241, 2, NULL, '5.5', 421, NUll);/*линейный коэффициент теплового расширения*/
INSERT into nano_f_answers VALUES (25, 4, NULL, 'электротехника', 421, NUll);/*области применения композита*/


INSERT INTO nano_l_composite VALUES (68, 'ГР.799', 'МК');
INSERT into nano_f_datum VALUES (422, 358, 68, 196, 67);
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.9', 422, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 422, Null);/*водопоглощение*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '300', 422, NUll);/*предел прочности при изгибе*/
INSERT into nano_f_answers VALUES (85, 34, NULL, '9.5', 422, NUll);/*Диэлектрическая проницаемость при 25°C и частоте 10⁶ Гц*/
INSERT into nano_f_answers VALUES (86, 8, NULL, '2', 422, NUll);/*Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶Гц*/
INSERT into nano_f_answers VALUES (87, 36, NULL, '100000000000000', 422, NUll);/*Удельное электрическое сопротивление*/
INSERT into nano_f_answers VALUES (241, 2, NULL, '6.5', 422, NUll);/*линейный коэффициент теплового расширения*/
INSERT into nano_f_answers VALUES (25, 4, NULL, 'электротехника', 422, NUll);/*области применения композита*/

INSERT INTO nano_l_questions VALUES (245, 7, 'Содержание матрицы в нанокомпозите', 'Matrix fraction of nanocomposite', '', 1);
INSERT INTO nano_l_answers VALUES (245, 1, 6, '%', '%');
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 418, NUll);
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 419, NUll);
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 420, NUll);
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 421, NUll);
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 422, NUll);

INSERT into nano_f_datum VALUES (423, 358, 61, 196, 67);
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 423, NUll);
INSERT into nano_f_answers VALUES (241, 3, NULL, '8', 423, NUll);

INSERT into nano_f_datum VALUES (424, 358, 62, 196, 67);
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 424, NUll);
INSERT into nano_f_answers VALUES (241, 1, NULL, '6', 424, NUll);
INSERT into nano_f_answers VALUES (241, 2, NULL, '7', 424, NUll);
INSERT into nano_f_answers VALUES (241, 3, NULL, '8', 424, NUll);

INSERT into nano_f_datum VALUES (425, 358, 63, 196, 67);
INSERT into nano_f_answers VALUES (245, 1, NULL, '95-99.5', 425, NUll);
INSERT into nano_f_answers VALUES (241, 1, NULL, '6', 425, NUll);
INSERT into nano_f_answers VALUES (241, 2, NULL, '7', 425, NUll);
INSERT into nano_f_answers VALUES (241, 37, NULL, '7.9', 425, NUll);
__________________________________________________________________________________________________________________________________________________________________________________________

INSERT INTO nano_l_articles VALUES 
(359, 'Fracture toughness of alumina–zirconia composites', 2006, 'Ceramics International', NULL, 29, '249-255', '7', '32', 'Cesari F., Esposito L., Furgiuele F.M., MAletta C., Tucci A.', 7, 'cesari2006.pdf');

INSERT INTO nano_l_fill VALUES (68, 2, 'ZrO2', 'Оксид циркония','Zinc oxide'); 
INSERT INTO nano_l_matrix VALUES (197, 12, 'ZrO2', 'Оксид циркония','Zinc oxide');
INSERT INTO nano_l_fill VALUES (69, 2, 'Al2O3', 'Оксид алюминия','Aluminum oxide');

INSERT INTO nano_l_composite VALUES (69, 'TZ3Y20A', '8Z2A', '80', '20');
INSERT into nano_f_datum VALUES (426, 359, 69, 197, 69);
INSERT INTO nano_l_questions VALUES (246, 10, 'площадь поверхности', 'surface area', '', 1);/*площадь поверхности*/
INSERT into nano_l_answers VALUES (246, 1, 2, 'м^2/г', 'm^2/g');
INSERT INTO nano_f_answers VALUES (246, 1, NULL, '16', 426, Null);
INSERT INTO nano_l_questions VALUES (247, 10, 'средний размер частиц матрицы', 'Average particles size of matrix', '', 1);/*средний размер частиц матррицы*/
INSERT into nano_l_answers VALUES (247, 1, 2, 'мкм', 'μm');
INSERT INTO nano_f_answers VALUES (247, 1, NULL, '0.1', 426, Null);
INSERT INTO nano_l_questions VALUES (248, 10, 'средний размер частиц наполнителя', 'Average particles size of fill', '', 1);/*средний размер частиц наполнителя*/
INSERT into nano_l_answers VALUES (248, 1, 2, 'мкм', 'μm');
INSERT INTO nano_f_answers VALUES (248, 1, NULL, '0.3', 426, Null);
INSERT into nano_f_answers VALUES (242, 31, NULL, '811', 426, null);/*предел прочности при изгибе*/
INSERT INTO nano_l_questions VALUES (249, 10, 'Модуль Вейбулла', 'Weibull modulus', '', 1);/*Модуль Вейбулла*/
INSERT into nano_l_answers VALUES (249, 1, 2, 'нет единиц измерения', 'without measure');
INSERT INTO nano_f_answers VALUES (249, 1, NULL, '13.3', 426, Null);
INSERT INTO nano_l_questions VALUES (250, 10, 'нормализованная прочность материала', 'normalised material strength', '', 1);/*нормализованная прочность материала*/
INSERT into nano_l_answers VALUES (250, 1, 2, 'МПа', 'MPa');
INSERT INTO nano_f_answers VALUES (250, 1, NULL, '831', 426, Null);
INSERT INTO nano_l_questions VALUES (251, 10, 'Твердость по Виккерсу', 'Vickers hardness', '', 1);/*Твердость по Виккерсу*/
INSERT into nano_l_answers VALUES (251, 1, 2, 'ГПа', 'GPa');
INSERT INTO nano_f_answers VALUES (251, 1, NULL, '246', 426, Null);
INSERT into nano_l_answers VALUES (71, 7, 10, 'ГПа', 'GPa');/*модуль упругости*/
INSERT into nano_f_answers VALUES (71, 7, NULL, '14.3', 426, NUll);
INSERT INTO nano_l_questions VALUES (252, 10, 'критический коэффициент интенсивности напряжений I моды деформаций', 'the critical stress intensity factor I of the deformation mode', '', 1);/*критический коэффициент интенсивности напряжений I моды деформаций*/
INSERT into nano_l_answers VALUES (252, 1, 2, 'МПа*м^(1/2)', 'MPa*m^(1/2)');
INSERT INTO nano_f_answers VALUES (252, 1, NULL, '5', 426, Null);
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '5.48', 426, Null);/*плoтность*/
INSERT INTO nano_l_questions VALUES (253, 10, 'Средний размер зерна матрицы', 'Average grain size of matrix', '', 1);/*Средний размер зерна матрицы*/
INSERT into nano_l_answers VALUES (253, 1, 2, 'мкм', 'μm');
INSERT INTO nano_f_answers VALUES (253, 1, NULL, '0.6', 426, Null);
INSERT INTO nano_l_questions VALUES (254, 10, 'Средний размер зерна наполнителя', 'Average grain size of fill', '', 1);/*Средний размер зерна наполнителя*/
INSERT into nano_l_answers VALUES (254, 1, 2, 'мкм', 'μm');
INSERT INTO nano_f_answers VALUES (254, 1, NULL, '0.68', 426, Null);
INSERT INTO nano_l_answers VALUES (22, 4, 3, 'Название метода', 'Method name');/*способ получения нанокомпозита*/
INSERT INTO nano_l_answers VALUES (159, 4, 3, 'Диапазон температур', 'Temperature range');
INSERT INTO nano_l_answers VALUES (162, 4, 3, 'Краткое описание', 'Short description');
INSERT INTO nano_f_answers VALUES (22, 4, NULL, 'Литьё под давлением', 426, Null);
INSERT INTO nano_f_answers VALUES (159, 4, NULL, '1500-1600 °C', 426, Null);
INSERT into nano_f_answers VALUES (162, 4, NULL, 'Метод литья под давлением. Сушка и спекание на воздухе при температурах от 1500 до 1600 °C, в зависимости от композиции порошка.', 426, null);


INSERT INTO nano_l_composite VALUES (70, 'TZ3Y40A', '6Z4A', '60', '40');
INSERT into nano_f_datum VALUES (427, 359, 70, 197, 69);
INSERT INTO nano_f_answers VALUES (246, 1, NULL, '15', 427, Null);/*площадь поверхности*/
INSERT INTO nano_f_answers VALUES (247, 1, NULL, '0.3', 427, Null);/*средний размер частиц матррицы*/
INSERT INTO nano_f_answers VALUES (248, 1, NULL, '0.3', 427, Null);/*средний размер частиц наполнителя*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '912', 427, null);/*предел прочности при изгибе*/
INSERT INTO nano_f_answers VALUES (249, 1, NULL, '7.3', 427, Null);/*Модуль Вейбулла*/
INSERT INTO nano_f_answers VALUES (250, 1, NULL, '977', 427, Null);/*нормализованная прочность материала*/
INSERT INTO nano_f_answers VALUES (251, 1, NULL, '285', 427, Null);/*Твердость по Виккерсу*/
INSERT into nano_f_answers VALUES (71, 7, NULL, '15.1', 427, NUll);/*модуль упругости*/
INSERT INTO nano_f_answers VALUES (252, 1, NULL, '5.2', 427, Null);/*критический коэффициент интенсивности напряжений I моды деформаций*/
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '5.02', 427, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (253, 1, NULL, '0.36', 427, Null);/*Средний размер зерна матрицы*/
INSERT INTO nano_f_answers VALUES (254, 1, NULL, '0.29', 427, Null);/*Средний размер зерна наполнителя*/
INSERT INTO nano_f_answers VALUES (22, 4, NULL, 'Литьё под давлением', 427, Null);/*способ получения нанокомпозита*/
INSERT INTO nano_f_answers VALUES (159, 4, NULL, '1500-1600 °C', 427, Null);
INSERT into nano_f_answers VALUES (162, 4, NULL, 'Метод литья под давлением. Сушка и спекание на воздухе при температурах от 1500 до 1600 °C, в зависимости от композиции порошка.', 427, null);


INSERT INTO nano_l_composite VALUES (71, 'TZ3Y60A', '4Z6A', '60', '40');
INSERT into nano_f_datum VALUES (428, 359, 71, 196, 68);
INSERT INTO nano_f_answers VALUES (246, 1, NULL, '12', 428, Null);/*площадь поверхности*/
INSERT INTO nano_f_answers VALUES (247, 1, NULL, '0.3', 428, Null);/*средний размер частиц матррицы*/
INSERT INTO nano_f_answers VALUES (248, 1, NULL, '0.3', 428, Null);/*средний размер частиц наполнителя*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '834', 428, null);/*предел прочности при изгибе*/
INSERT INTO nano_f_answers VALUES (249, 1, NULL, '11.4', 428, Null);/*Модуль Вейбулла*/
INSERT INTO nano_f_answers VALUES (250, 1, NULL, '874', 428, Null);/*нормализованная прочность материала*/
INSERT INTO nano_f_answers VALUES (251, 1, NULL, '316', 428, Null);/*Твердость по Виккерсу*/
INSERT into nano_f_answers VALUES (71, 7, NULL, '16.4', 428, NUll);/*модуль упругости*/
INSERT INTO nano_f_answers VALUES (252, 1, NULL, '3.6', 428, Null);/*критический коэффициент интенсивности напряжений I моды деформаций*/
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '4.6', 428, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (253, 1, NULL, '0.37', 428, Null);/*Средний размер зерна матрицы*/
INSERT INTO nano_f_answers VALUES (254, 1, NULL, '0.25', 428, Null);/*Средний размер зерна наполнителя*/
INSERT INTO nano_f_answers VALUES (22, 4, NULL, 'Литьё под давлением', 428, Null);/*способ получения нанокомпозита*/
INSERT INTO nano_f_answers VALUES (159, 4, NULL, '1500-1600 °C', 428, Null);
INSERT into nano_f_answers VALUES (162, 4, NULL, 'Метод литья под давлением. Сушка и спекание на воздухе при температурах от 1500 до 1600 °C, в зависимости от композиции порошка.', 428, null);


INSERT INTO nano_l_composite VALUES (72, 'TZ3Y80A', '2Z8A', '80', '20');
INSERT into nano_f_datum VALUES (429, 359, 72, 196, 68);
INSERT INTO nano_f_answers VALUES (246, 1, NULL, '10', 429, Null);/*площадь поверхности*/
INSERT INTO nano_f_answers VALUES (247, 1, NULL, '0.1', 429, Null);/*средний размер частиц матррицы*/
INSERT INTO nano_f_answers VALUES (248, 1, NULL, '0.3', 429, Null);/*средний размер частиц наполнителя*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '717', 429, null);/*предел прочности при изгибе*/
INSERT INTO nano_f_answers VALUES (249, 1, NULL, '7.7', 429, Null);/*Модуль Вейбулла*/
INSERT INTO nano_f_answers VALUES (250, 1, NULL, '763', 429, Null);/*нормализованная прочность материала*/
INSERT INTO nano_f_answers VALUES (251, 1, NULL, '348', 429, Null);/*Твердость по Виккерсу*/
INSERT into nano_f_answers VALUES (71, 7, NULL, '17.8', 429, NUll);/*модуль упругости*/
INSERT INTO nano_f_answers VALUES (252, 1, NULL, '4.1', 429, Null);/*критический коэффициент интенсивности напряжений I моды деформаций*/
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '4.27', 429, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (253, 1, NULL, '0.6', 429, Null);/*Средний размер зерна матрицы*/
INSERT INTO nano_f_answers VALUES (254, 1, NULL, '0.25', 429, Null);/*Средний размер зерна наполнителя*/
INSERT INTO nano_f_answers VALUES (22, 4, NULL, 'Литьё под давлением', 429, Null);/*способ получения нанокомпозита*/
INSERT INTO nano_f_answers VALUES (159, 4, NULL, '1500-1600 °C', 429, Null);
INSERT into nano_f_answers VALUES (162, 4, NULL, 'Метод литья под давлением. Сушка и спекание на воздухе при температурах от 1500 до 1600 °C, в зависимости от композиции порошка.', 429, null);


INSERT INTO nano_l_fill VALUES (70, 14, 'нет наполнителя', 'без наполнителя', 'without fill')
INSERT INTO nano_l_composite VALUES (73, 'SM8', 'Baikowski, F', '100', '0');
INSERT into nano_f_datum VALUES (430, 359, 73, 196, 70);
INSERT INTO nano_f_answers VALUES (246, 1, NULL, '10', 430, Null);/*площадь поверхности*/
INSERT INTO nano_f_answers VALUES (247, 1, NULL, '0.2', 430, Null);/*средний размер частиц матррицы*/
INSERT INTO nano_f_answers VALUES (248, 1, NULL, '0', 430, Null);/*средний размер частиц наполнителя*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '436', 430, null);/*предел прочности при изгибе*/
INSERT INTO nano_f_answers VALUES (249, 1, NULL, '9.7', 430, Null);/*Модуль Вейбулла*/
INSERT INTO nano_f_answers VALUES (250, 1, NULL, '460', 430, Null);/*нормализованная прочность материала*/
INSERT INTO nano_f_answers VALUES (251, 1, NULL, '356', 430, Null);/*Твердость по Виккерсу*/
INSERT into nano_f_answers VALUES (71, 7, NULL, '18.3', 430, NUll);/*модуль упругости*/
INSERT INTO nano_f_answers VALUES (252, 1, NULL, '4.2', 430, Null);/*критический коэффициент интенсивности напряжений I моды деформаций*/
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.95', 430, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (253, 1, NULL, '3.4', 430, Null);/*Средний размер зерна матрицы*/
INSERT INTO nano_f_answers VALUES (254, 1, NULL, '0', 430, Null);/*Средний размер зерна наполнителя*/


INSERT INTO nano_l_composite VALUES (74, 'TZ3YS', 'TZ3YS—Tosoh Co., J, 3 mol% Y2O3', '100', '0');
INSERT into nano_f_datum VALUES (431, 359, 74, 197, 70);
INSERT INTO nano_f_answers VALUES (246, 1, NULL, '7', 431, Null);/*площадь поверхности*/
INSERT INTO nano_f_answers VALUES (247, 1, NULL, '0.2', 431, Null);/*средний размер частиц матррицы*/
INSERT INTO nano_f_answers VALUES (248, 1, NULL, '0', 431, Null);/*средний размер частиц наполнителя*/
INSERT into nano_f_answers VALUES (242, 31, NULL, '766', 431, null);/*предел прочности при изгибе*/
INSERT INTO nano_f_answers VALUES (249, 1, NULL, '11.7', 431, Null);/*Модуль Вейбулла*/
INSERT INTO nano_f_answers VALUES (250, 1, NULL, '810', 431, Null);/*нормализованная прочность материала*/
INSERT INTO nano_f_answers VALUES (251, 1, NULL, '205', 431, Null);/*Твердость по Виккерсу*/
INSERT into nano_f_answers VALUES (71, 7, NULL, '13.3', 431, NUll);/*модуль упругости*/
INSERT INTO nano_f_answers VALUES (252, 1, NULL, '4.3', 431, Null);/*критический коэффициент интенсивности напряжений I моды деформаций*/
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '6.05', 431, Null);/*плoтность*/
INSERT INTO nano_f_answers VALUES (253, 1, NULL, '0.74', 431, Null);/*Средний размер зерна матрицы*/
INSERT INTO nano_f_answers VALUES (254, 1, NULL, '0', 431, Null);/*Средний размер зерна наполнителя*/
