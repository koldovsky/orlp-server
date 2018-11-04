insert into deck (
    description,
    name,
    rating,
    category_id,
    user_id,
    syntax,
    created_by)
values (
    'Questions regarding Features that were implemented in Java 8',
    'Java 8 Features',
     0,
     1,
     1,
     'JAVA',
     1);

SET @deck_id = (SELECT max(deck_id) from deck where name = 'Java 8 Features');

insert into card (
    answer,
    question,
    rating,
    title,
    deck_id,
    created_by)
values (
    'String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(", "));\r\nSystem.out.println(G7Countries);',
    'Convert String to UpperCase and join them using coma\r\nList<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");',
    0,
    'Card 1',
    @DECK_ID,
    1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'The key difference between Anonymous class and Lambda expression is the usage of ‘this’ keyword. In the anonymous classes, ‘this’ keyword resolves to anonymous class itself, whereas for lambda expression ‘this’ keyword resolves to enclosing class where lambda expression is written.\r\nAnother difference between lambda expression and anonymous class is in the way these two are compiled. Java compiler compiles lambda expressions and convert them into private method of the class. It uses invokedynamic instruction that was added in Java 7 to bind this method dynamically.',
	'What is difference between Lambda Expression and Anonymous class?',
	0,
	'Card 2',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'public class RunnableLambda {\r\n\tpublic static void main(String[] args) {\r\n\t\t// Runnable using lambda\r\n\t\tnew Thread(()->System.out.println("Runnable as Lambda expression")).start();\r\n\t}\r\n}\r\nor\r\npublic class RunnableLambda {\r\n\tpublic static void main(String[] args) {\r\n\t\tRunnable r = ()->{System.out.println("Runnable as Lambda expression");};\r\n\t\t// Passing runnable instance\r\n\t\tnew Thread(r).start();\r\n\t}\r\n}',
	'How can we write runnable as lambda expression?\r\npublic class RunnableIC {\r\n\tpublic static void main(String[] args) {\r\n\t\t// Runnable using anonymous class\r\n\t\tnew Thread(new Runnable() {\r\n\t\t\t@Override\r\n\t\t\tpublic void run() {\r\n\t\t\t\tSystem.out.println("Runnable as anonymous class");\r\n\t\t\t}\r\n\t\t}).start();\r\n\t}\r\n}',
	0,
	'Card 3',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Collections.sort(personList, (Person p1, Person p2) -> p1.name.compareTo(p2.name));\r\nor\r\npersonList.sort((p1, p2) -> p1.name.compareTo(p2.name));',
	'Sort personList by name using lambda expression\r\nList<Person> personList = new ArrayList<>();\r\npersonList.add(new Person("Virat", 25));\r\npersonList.add(new Person("Arun", 44));\r\npersonList.add(new Person("Rajesh", 35));\r\npersonList.add(new Person("Rahul", 29));',
	0,
	'Card 4',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Lambda expression doesn`t have a type of its own. A lambda expression provides implementation of the abstract method defined by the functional interface. Thus the functional interface specifies its target type.\r\nLambda supports "target typing" which infers the object type from the context in which it is used.\r\nTo infer that object type from the context -\r\nThe parameter type of the abstract method and the parameter type of the lambda expression must be compatible. For Example, if the abstract method in the functional interface specifies one int parameter, then the lambda should also have one int parameter explicitly defined or implicitly inferred as int by the context.\r\nIts return type must be compatible with the method`s type.\r\nLambda expression can throw only those exceptions which are acceptable to the method.',
	'How target type is inferred for the lambda expression?',
	0,
	'Card 5',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Functional interface is an interface with no more, no less but one single abstract method (default methods do not count)',
	'What is a functional interface?',
	0,
	'Card 6',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'The major benefit of java 8 functional interfaces is that we can use lambda expressions to instantiate them and avoid using bulky anonymous class implementation.',
	'What are the major benefit of java 8 functional interfaces?',
	0,
	'Card 7',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Function – it takes one argument and returns a result\r\nConsumer – it takes one argument and returns no result (represents a side effect)\r\nSupplier – it takes not argument and returns a result\r\nPredicate – it takes one argument and returns a boolean\r\nBiFunction – it takes two arguments and returns a result\r\nBinaryOperator – it is similar to a BiFunction, taking two arguments and returning a result. The two arguments and the result are all of the same types\r\nUnaryOperator – it is similar to a Function, taking a single argument and returning a result of the same type',
	'Describe some of the functional interfaces in the standard library.',
	0,
	'Card 8',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'@FunctionalInterface\r\ninterface MyFunctionalInterface {\r\n\tpublic int addMethod(int a, int b);\r\n}\r\npublic class BeginnersBookClass {\r\n\tpublic static void main(String args[]) {\r\n\t\tMyFunctionalInterface sum = (a, b) -> a + b;\r\n\t\tSystem.out.println("Result: "+sum.addMethod(12, 100));\r\n\t}\r\n}',
	'reate a functional interface with a method, that accepts two parameters and return their sum. Then implement this interface in main() method using lambda expression. Output the result in the console by System.out.println();',
	0,
	'Card 9',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'import java.util.function.Consumer;\r\n\r\npublic class Main {\r\n\tpublic static void main(String[] args) {\r\n\t\tConsumer<String> c = (x) -> System.out.println(x.toLowerCase());\r\n\t\tc.accept("hEllo World!");\r\n\t}\r\n}',
	'Using Consumer interface convert String “hEllo World!” to LowerCase',
	0,
	'Card 10',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'When our Java project wants to perform the following operations, it’s better to use Java 8 Stream API to get lot of benefits:\r\nWhen we want perform Database like Operations. For instance, we want perform groupby operation, orderby operation etc.\r\nWhen want to Perform operations Lazily.\r\nWhen we want to write Functional Style programming.\r\nWhen we want to perform Parallel Operations.\r\nWhen want to use Internal Iteration\r\nWhen we want to perform Pipelining operations.\r\nWhen we want to achieve better performance.',
	'When do we go for Java 8 Stream API? Why do we need to use Java 8 Stream API in our projects?',
	0,
	'Card 11',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Stream<Integer> stream = Stream.of(1,2,3,4);',
	'How to create a Stream of integers',
	0,
	'Card 12',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Stream<Integer> intStream = Stream.of(1,2,3,4);\r\nInteger[] intArray = intStream.toArray(Integer[]::new);\r\nSystem.out.println(Arrays.toString(intArray)); //prints [1, 2, 3, 4]',
	'How to Convert a Java Stream of Integers to Collection or Array',
	0,
	'Card 13',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'We can use map() method \r\nStream<String> names = Stream.of("aBc", "d", "ef");\r\nSystem.out.println(names.map(s -> {\r\n\t\treturn s.toUpperCase();\r\n\t}).collect(Collectors.toList()));\r\n//prints [ABC, D, EF]',
	'How do we apply functions to a stream?',
	0,
	'Card 14',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'We can use reduce()\r\nStream<Integer> numbers = Stream.of(1,2,3,4,5);\r\nOptional<Integer> intOptional = numbers.reduce((i,j) -> {return i*j;});\r\nif(intOptional.isPresent()) System.out.println("Multiplication = "+intOptional.get()); //120',
	'How do we perform a reduction on the elements of the stream',
	0,
	'Card 15',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'/Get the current date\r\nLocalDate today = LocalDate.now();\r\nSystem.out.println("Current date: " + today);',
	'How will you get the current date using local datetime api of java8?',
	0,
	'Card 16',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'//add 1 week to the current date\r\nLocalDate today = LocalDate.now();\r\nLocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);\r\nSystem.out.println("Next week: " + nextWeek);',
	'How will you add 1 week to current date using local datetime api of java8?',
	0,
	'Card 17',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'/get the next tuesday\r\nLocalDate today = LocalDate.now();\r\nLocalDate nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));\r\nSystem.out.println("Next Tuesday on : " + nextTuesday);',
	'How will you get next tuesday using java8?',
	0,
	'Card 18',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Instant now = currentDate.toInstant();\r\nZoneId currentZone = ZoneId.systemDefault();\r\nLocalDateTime localDateTime = LocalDateTime.ofInstant(now, currentZone);\r\nSystem.out.println("Local date: " + localDateTime);',
	'How will you get the instant of local date time using time in of milliseconds using java8?',
	0,
	'Card 19',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Instant now = currentDate.toInstant();\r\nZoneId currentZone = ZoneId.systemDefault();\r\nZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, currentZone);\r\nSystem.out.println("Zoned date: " + zonedDateTime);',
	'How will you get the instant of zoned date time using time in of milliseconds using java8?',
	0,
	'Card 20',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'interface MathInterface {\t\r\n\t//static method\r\n\tstatic void square(int a){\r\n\tSystem.out.println(a*a)\r\n\t}\r\n}',
	'Write interface with static method to show the square of number',
	0,
	'Card 21',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Default methods allow us to add new methods to an interface that are automatically available in the implementations.',
	'When do we use default methods in interface?',
	0,
	'Card 22',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Static Methods are the utility(util) methods which are associated to an Interface. So, in case you need any util methods which can operate on an Interface’s implementing class’s instances, then add that as a static method to the Interface itself.',
	'When do we use static methods in interface?',
	0,
	'Card 23',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Using keyword supper:\r\npublic class car implements vehicle, fourWheeler {\r\n\r\n\tdefault void print() {\r\n\t\tvehicle.super.print();\r\n\t}\r\n}',
	'You have class that implements two interfaces with default methods with the same signature. How can you call default method from particular interface(for example call method from interface ‘vehicle’)?\r\npublic interface vehicle {\r\n\r\n\tdefault void print() {\r\n\t\tSystem.out.println("I am a vehicle!");\r\n\t}\r\n}\r\n\r\npublic interface fourWheeler {\r\n\r\n\tdefault void print() {\r\n\t\tSystem.out.println("I am a four wheeler!");\r\n\t}\r\n}',
	0,
	'Card 24',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'String str = "Hello my dear friend";\r\nopt = opt.of(str);\r\nSystem.out.println(opt.get());',
	'The following code below throws NullPointerException. Change code to valid one using method from Optional class.\r\n\r\nprivate static Optional<String> opt;\r\npublic static void main(String ... args) {\r\n\tString str = "Hello my dear friend";\r\n\tSystem.out.println(opt.get());\r\n}',
	0,
	'Card 25',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Optional<User> user = Optional.of(repository.findById(userId));',
	'You have the following code. Please, rewrite it by using Optional parametrized by User class into one line User user;\r\nif (Objects.nonNull(user =  repository.findById(userId))) {\r\n\t// some logic is here\r\n}',
	0,
	'Card 26',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Optional is a good way to protect application from runtime nullPointerException in case the the absent value has been represented as null. So basically Optional class provides the type checking during compile time and hence will never result in NPE.',
	'What is the use of Optional?',
	0,
	'Card 27',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'Optional<Address> op = p.getAddress();\r\nop.isPresent(System.out::println);',
	'Replace the following code using Optional\r\nPerson p = new Person("Robin", new Address(block, city, state, country); Address a = p.getAddress(); if(a != null){ System.out.println(p); }',
	0,
	'Card 28',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'String name = Optional.ofNullable(nullName).orElseThrow(RuntimeException::new);',
	'The following code return default value  if nullName variable is null. Rewrite the code which will throw RuntimeException instead of return “hello”. \r\npublic static void main(String[] args) {\r\n\tString nullName = null;\r\n\tString name = Optional.ofNullable(nullName).orElseGet(Main::getDefaultMessage);\r\n}\r\n\r\nprivate static String getDefaultMessage() {\r\n\treturn "Default message";\r\n}',
	0,
	'Card 29',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'A method reference to: a static method, an instance method of an object of a particular type, an instance method of an existing object, a constructor.',
	'Which types of method references do you know?',
	0,
	'Card 30',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'A method reference is a Java 8 construct that can be used for referencing a method without invoking it. It is used for treating methods as Lambda Expressions. They only work as syntactic sugar to reduce the verbosity of some lambdas.',
	'Where and when can we use method references?',
	0,
	'Card 31',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'ObjectType::instanceMethod',
	'In this case, we have a lambda expression like the following:\r\n(obj, args) -> obj.instanceMethod(args)\r\nPlease, turn it into method reference.',
	0,
	'Card 32',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'BiFunction<String, String, Locale> f = Locale::new;\r\nLocale loc = f.apply(“en”, “UK”);',
	'We have a lambda expression:\r\nBiFunction<String, String, Locate> f = (lang, country) -> new Locale(lang,country);\r\nLocale loc = f.apply(“en”, “UK”);\r\nPlease, turn it into method reference.',
	0,
	'Card 33',
	@DECK_ID,
	1);

insert into card (
	answer,
	question,
	rating,
	title,
	deck_id,
	created_by)
values (
	'It is a static method reference to the valueOf method of the String class.',
	'What is the meaning of String::valueOf expression?',
	0,
	'Card 34',
	@DECK_ID,
	1);

