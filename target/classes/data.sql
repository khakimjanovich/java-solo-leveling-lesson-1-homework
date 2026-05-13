insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'JAVA_CORE', 'EASY', 'MULTIPLE_CHOICE', 'Which keyword is used to define a class in Java?', 'class', 'function', 'define', 'a', 'Java classes are declared with the class keyword.'
where not exists (select 1 from questions where question_text = 'Which keyword is used to define a class in Java?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'JAVA_CORE', 'EASY', 'MULTIPLE_CHOICE', 'Which method is the standard Java application entry point?', 'start', 'run', 'main', 'c', 'A Java application starts from public static void main(String[] args).'
where not exists (select 1 from questions where question_text = 'Which method is the standard Java application entry point?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'JAVA_CORE', 'MEDIUM', 'MULTIPLE_CHOICE', 'Which Java feature lets you group related constants safely?', 'enum', 'array', 'package', 'a', 'Enums model a fixed set of named constants.'
where not exists (select 1 from questions where question_text = 'Which Java feature lets you group related constants safely?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'JAVA_CORE', 'HARD', 'OPEN', 'What keyword prevents a variable from being reassigned?', null, null, null, 'final', 'final prevents reassignment of a variable after initialization.'
where not exists (select 1 from questions where question_text = 'What keyword prevents a variable from being reassigned?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'OOP', 'EASY', 'MULTIPLE_CHOICE', 'Which keyword creates an inheritance relationship?', 'extends', 'inherits', 'parent', 'a', 'A Java class extends another class.'
where not exists (select 1 from questions where question_text = 'Which keyword creates an inheritance relationship?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'OOP', 'MEDIUM', 'MULTIPLE_CHOICE', 'Which concept hides internal object state behind methods?', 'encapsulation', 'compilation', 'serialization', 'a', 'Encapsulation hides internal state and exposes controlled behavior.'
where not exists (select 1 from questions where question_text = 'Which concept hides internal object state behind methods?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'OOP', 'HARD', 'OPEN', 'What keyword allows a class to satisfy an interface contract?', null, null, null, 'implements', 'A class implements an interface.'
where not exists (select 1 from questions where question_text = 'What keyword allows a class to satisfy an interface contract?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'COLLECTIONS', 'EASY', 'MULTIPLE_CHOICE', 'Which collection keeps elements in insertion order and allows duplicates?', 'List', 'Set', 'Map', 'a', 'A List preserves order and allows duplicate elements.'
where not exists (select 1 from questions where question_text = 'Which collection keeps elements in insertion order and allows duplicates?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'COLLECTIONS', 'MEDIUM', 'MULTIPLE_CHOICE', 'Which collection type stores unique values?', 'List', 'Set', 'Queue', 'b', 'Set implementations do not allow duplicate elements.'
where not exists (select 1 from questions where question_text = 'Which collection type stores unique values?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'COLLECTIONS', 'HARD', 'OPEN', 'Which Map implementation is commonly used for hash-based key lookup?', null, null, null, 'HashMap', 'HashMap is the common hash-table based Map implementation.'
where not exists (select 1 from questions where question_text = 'Which Map implementation is commonly used for hash-based key lookup?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'EXCEPTIONS', 'EASY', 'MULTIPLE_CHOICE', 'Which block always runs after try/catch when present?', 'finally', 'always', 'after', 'a', 'finally runs after try/catch except in extreme JVM termination cases.'
where not exists (select 1 from questions where question_text = 'Which block always runs after try/catch when present?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'EXCEPTIONS', 'MEDIUM', 'MULTIPLE_CHOICE', 'What keyword is used to handle an exception?', 'catch', 'rescue', 'handle', 'a', 'try/catch handles exceptions.'
where not exists (select 1 from questions where question_text = 'What keyword is used to handle an exception?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'EXCEPTIONS', 'HARD', 'OPEN', 'What keyword declares that a method may pass an exception to its caller?', null, null, null, 'throws', 'throws declares checked exceptions on a method signature.'
where not exists (select 1 from questions where question_text = 'What keyword declares that a method may pass an exception to its caller?');

insert into questions (topic, difficulty, type, question_text, optiona, optionb, optionc, correct_answer, explanation)
select 'MAVEN', 'EASY', 'OPEN', 'What file defines Maven dependencies?', null, null, null, 'pom.xml', 'Maven projects are configured through pom.xml.'
where not exists (select 1 from questions where question_text = 'What file defines Maven dependencies?');
