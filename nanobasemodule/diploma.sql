ALTER TABLE nano_l_answers ADD FOREIGN KEY (ANSWER_TYPE_ID) REFERENCES nano_l_answers_types(ANSWER_TYPE_ID)
ALTER TABLE nano_l_fill ADD FOREIGN KEY (FILL_CATEGORY) REFERENCES category_fill(id)
ALTER TABLE nano_l_questions ADD FOREIGN KEY (QUESTION_GROUP_ID) REFERENCES nano_l_question_groups(QUESTION_GROUP_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (COMPOSITE_ID) REFERENCES nano_l_composite(id)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (MATRIX_ID) REFERENCES nano_l_matrix(MATRIX_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (FILL_ID) REFERENCES nano_l_fill(FILL_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (ARTICLE_ID) REFERENCES nano_l_articles(ARTICLE_ID)	
ALTER TABLE nano_l_articles ADD FOREIGN KEY (COUNTRY_ID) REFERENCES nano_countries(COUNTRY_ID)
ALTER TABLE nano_l_matrix ADD FOREIGN KEY (MATRIX_CATEGORY) REFERENCES category_matrix(id)

CREATE VIEW articles AS
SELECT nlc.name, nla.ARTICLE_NAME, nla.YEAR_ID, nla.JOURNAL_NAME, nla.AUTHORS, nc.COUNTRY_NAME
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
	WHERE nlq.QUESTION_ID = nla.QUESTION_ID AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
	AND nlm.MATRIX_NAME = matrixName
    AND nlq.QUESTION_GROUP_ID <> 4
    AND nlq.QUESTION_GROUP_ID <> 3;
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
