This code is basic database system program. It supports some querries and it can store several tables.

Adding data to program:
	In order to add data with csv format to program, user need to type:	CREATE TABLE FROM ~~pathfile~~ command. It can store multiple tables.
Filtering data from program:
	User can filter data with several parameters of SELECT.
	SELECT ~~singleColumnName~~ FROM ~~tablename~~		List only one column of table.
	SELECT ~~commaSeperatedColumList~~ FROM ~~tablename~~	List several columns of table.
	SELECT * FROM ~~tablename~~					List all columns of table.
	
	User can add extra filters.
	SELECT ~~columnOrColumnFamily~~ FROM ~~tablename~~ WHERE columnName=SomeValue;	List only one column of table.
	
This program works for this inputs but throw exception for wrong inputs.
