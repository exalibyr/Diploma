ALTER TABLE nano_l_answers ADD FOREIGN KEY (ANSWER_TYPE_ID) REFERENCES nano_l_answers_types(ANSWER_TYPE_ID)
ALTER TABLE nano_l_fill ADD FOREIGN KEY (FILL_CATEGORY) REFERENCES category_fill(id)
ALTER TABLE nano_l_questions ADD FOREIGN KEY (QUESTION_GROUP_ID) REFERENCES nano_l_question_groups(QUESTION_GROUP_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (COMPOSITE_ID) REFERENCES nano_l_composite(id)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (MATRIX_ID) REFERENCES nano_l_matrix(MATRIX_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (FILL_ID) REFERENCES nano_l_fill(FILL_ID)
ALTER TABLE nano_f_datum ADD FOREIGN KEY (ARTICLE_ID) REFERENCES nano_l_articles(ARTICLE_ID)	
ALTER TABLE nano_l_articles ADD FOREIGN KEY (COUNTRY_ID) REFERENCES nano_countries(COUNTRY_ID)
ALTER TABLE nano_l_matrix ADD FOREIGN KEY (MATRIX_CATEGORY) REFERENCES category_matrix(id)

ALTER TABLE nano_l_composite ADD MATRIX_FRACTION TEXT
ALTER TABLE nano_l_composite ADD FILL_FRACTION TEXT

CREATE VIEW articles AS
SELECT nlc.name, nla.ARTICLE_NAME, nla.YEAR_ID, nla.JOURNAL_NAME, nla.AUTHORS, nc.COUNTRY_NAME
FROM nano_f_datum nfd, nano_l_articles nla, nano_l_composite nlc, nano_countries nc, nano_l_matrix nlm, category_matrix cm
WHERE nla.ARTICLE_ID = nfd.ARTICLE_ID
AND nlc.id = nfd.COMPOSITE_ID
AND nc.COUNTRY_ID = nla.COUNTRY_ID
AND nfd.MATRIX_ID = nlm.MATRIX_ID
AND nlm.MATRIX_CATEGORY = cm.id
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
	SELECT DISTINCT nlq.QUESTION_NAME_ENG 
	FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm
	WHERE nlq.QUESTION_ID = nla.QUESTION_ID
    AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
	AND nlm.MATRIX_NAME = matrixName;
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
SELECT nlm.MATRIX_NAME, nlf.FILL_NAME, nlq.QUESTION_NAME_ENG, nfa.ANSWER_TEXT, nla.ANSWER_NAME
FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd, nano_l_matrix nlm, nano_l_fill nlf, category_matrix cm
WHERE nlq.QUESTION_ID = nla.QUESTION_ID
	AND nla.ANSWER_ID = nfa.ANSWER_ID
    AND nla.QUESTION_ID = nfa.QUESTION_ID
	AND nfa.DATA_ID = nfd.DATA_ID
	AND nfd.MATRIX_ID = nlm.MATRIX_ID
    AND nlf.FILL_ID = nfd.FILL_ID
    AND cm.id = nlm.MATRIX_CATEGORY
    AND cm.name = 'Керамика'
    AND nfd.DATA_ID <> 367
    
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
INSERT INTO nano_f_answers VALUES (123, 2, NULL, '0.1-0.2', 415, NULL)//процент наполнителя
INSERT INTO nano_f_answers VALUES (240, 23, NULL, '3.96', 415, Null)//плтность
INSERT INTO nano_l_answers VALUES (63, 30, 2, '%', '%')//водопоглощение
INSERT INTO nano_f_answers VALUES (63, 30, NULL, '0.02', 415, Null)
INSERT into nano_l_answers VALUES (242, 31, 2, 'МПА', 'MPa')//предел прочности при изгибе
INSERT into nano_f_answers VALUES (242, 31, NULL, '280', 415, NUll)
INSERT into nano_l_answers VALUES (71, 32, 2, 'Е•10-2, ГПа', 'Е•10-2, GPa')//модуль упругости
INSERT into nano_f_answers VALUES (71, 32, NULL, '3.5', 415, NUll)
INSERT into nano_l_answers VALUES (85, 34, 2, 'при 25°C и частоте 10⁶ Гц', 'at 25°C and frequency 10⁶ Hz')//Диэлектрическая проницаемость при 25оС и частоте 106 Гц
INSERT into nano_f_answers VALUES (85, 34, NULL, '10.3', 415, NUll)
//Тангенс угла диэлектрических потерь tgδ•10⁴, при 25°C и частоте 10⁶/10⁹ Гц
INSERT into nano_l_answers VALUES (86, 35, 2, 'tgδ•10⁴, при 25°C и частоте 10⁶/10⁹ Гц', 'tgδ•10⁴, at 25°C and frecuency 10⁶/10⁹ Hz') 
INSERT into nano_f_answers VALUES (86, 35, NULL, '2', 415, NUll)
INSERT into nano_l_answers VALUES (87, 36, 2, ', Ом•см, при 100°С', 'Ohm•cm, at 100°С')//Удельное электрическое сопротивление
INSERT into nano_f_answers VALUES (87, 36, NULL, '100000000000000', 415, NUll)
INSERT into nano_l_answers VALUES (241, 37, 2, 'α•10⁶ при 20-900°C, K⁻1', 'α•10⁶ at 20-900°C, K⁻1')//линейный коэффициент теплового расширения
INSERT into nano_f_answers VALUES (241, 37, NULL, '8.5', 415, NUll)
__________________________________________________________________________________________________________________________________________________________________________________________

INSERT INTO nano_l_answers VALUES (53, 33, 2, 'Вт/(м•К)', 'W/(K•m)')//Коэффициент теплопроводности





