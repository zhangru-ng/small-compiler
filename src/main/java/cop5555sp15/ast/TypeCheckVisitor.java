package cop5555sp15.ast;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TypeConstants;
import cop5555sp15.symbolTable.SymbolTable;
import static cop5555sp15.TokenStream.Kind.*;

public class TypeCheckVisitor implements ASTVisitor, TypeConstants {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		ASTNode node;

		public TypeCheckException(String message, ASTNode node) {
			super(node.firstToken.getText() + " " + node.firstToken.lineNumber + ":" + message);
			this.node = node;
		}
	}

	SymbolTable symbolTable;

	public TypeCheckVisitor(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	boolean check(boolean condition, String message, ASTNode node)
			throws TypeCheckException {
		if (condition)
			return true;
		throw new TypeCheckException(message, node);
	}

	/**
	 * Ensure that types on left and right hand side are compatible.
	 */
	@Override
	public Object visitAssignmentStatement(
			AssignmentStatement assignmentStatement, Object arg)
			throws Exception {
		String lvType = (String) assignmentStatement.lvalue.visit(this, arg);
		String exprType = (String) assignmentStatement.expression.visit(this, arg);
		if(lvType.equals(intType) || lvType.equals(booleanType) || lvType.equals(stringType)) {
			check(lvType.equals(exprType), "uncompatible assignment type", assignmentStatement);
		} else if (lvType.substring(0, lvType.indexOf("<")).equals("Ljava/util/List")) {
			if (exprType.substring(0, lvType.indexOf("<")).equals("Ljava/util/List")) {
				check(exprType.equals(lvType), "uncompatible assignment type", assignmentStatement);
			} else if (!exprType.equals(emptyList)) {
				String elementType = lvType.substring(lvType.indexOf("<") + 1, lvType.lastIndexOf(">"));
				String listType = "Ljava/util/ArrayList<" + elementType + ">;";
				check(exprType.equals(listType), "uncompatible assignment type", assignmentStatement);
			}
		} else {//if (lvType.substring(0, lvType.indexOf("<")).equals("Ljava/util/Map$Entry")){
			throw new UnsupportedOperationException("Map is not support yet");
		}		
		return null;		
	}

	/**
	 * Ensure that both types are the same, save and return the result type
	 *		int (+ | - | * | /) int 				-> int
	 *      string + string         				-> string
	 *      int (== | != | < | <= | >= | >) int     -> boolean
	 *      string (== | !=) string       			-> boolean
	 */
	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression,
			Object arg) throws Exception {
		String expr0Type = (String) binaryExpression.expression0.visit(this,arg);
		String expr1Type = (String) binaryExpression.expression1.visit(this,arg);
		Kind op = binaryExpression.op.kind;
		check(expr0Type.equals(expr1Type), "uncompatible bianry expression", binaryExpression);
		switch(op) {
		case PLUS:
			check(expr0Type.equals(intType) || expr0Type == stringType, "operator " + op.toString() + " is not defined for " + expr0Type, binaryExpression);
			break;
		case MINUS:	case TIMES:	case DIV:			
			check(expr0Type.equals(intType), "operator " + op.toString() + " is not defined for " + expr0Type, binaryExpression);
			break;
		case EQUAL:	case NOTEQUAL:
			if (expr0Type.equals(booleanType) || expr0Type.equals(intType) ||expr0Type.equals(stringType)) {
				binaryExpression.setType(booleanType);
				return booleanType;
			} else {
				throw new TypeCheckException("operator " + op.toString() + " is not defined for " + expr0Type, binaryExpression);
			}	
		case LT: case GT: case LE: case GE:
			if (expr0Type.equals(booleanType) || expr0Type.equals(intType)) {
				binaryExpression.setType(booleanType);
				return booleanType;
			} else {
				throw new TypeCheckException("operator " + op.toString() + " is not defined for " + expr0Type, binaryExpression);
			}		
		case LSHIFT: case RSHIFT:
			check(expr0Type.equals(intType), "operator " + op.toString() + " is not defined for " + expr0Type, binaryExpression);
			break;
		case BAR: case AND:
			check(expr0Type.equals(booleanType), "operator " + op.toString() + " is not defined for " + expr0Type, binaryExpression);
			break;
		default:
			throw new TypeCheckException("operator " + op.toString() + " is not defined for " + expr0Type, binaryExpression);
		} 	
		binaryExpression.setType(expr0Type);
		return expr0Type;
	}

	/**
	 * Blocks define scopes. Check that the scope nesting level is the same at
	 * the end as at the beginning of block
	 */
	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		int numScopes = symbolTable.enterScope();
		// visit children
		for (BlockElem elem : block.elems) {
			elem.visit(this, arg);
		}
		int numScopesExit = symbolTable.leaveScope();
		check(numScopesExit > 0 && numScopesExit == numScopes,
				"unbalanced scopes", block);
		return null;
	}

	/**
	 * Sets the expressionType to booleanType and returns it
	 * 
	 * @param booleanLitExpression
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object visitBooleanLitExpression(
			BooleanLitExpression booleanLitExpression, Object arg)
			throws Exception {
		booleanLitExpression.setType(booleanType);
		return booleanType;
	}

	/**
	 * A closure defines a new scope Visit all the declarations in the
	 * formalArgList, and all the statements in the statementList construct and
	 * set the JVMType, the argType array, and the result type
	 * 
	 * @param closure
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object visitClosure(Closure closure, Object arg) throws Exception {
//		List<String> argTypes = new ArrayList<String>();
//		String resultType = null;
//		int numScopes = symbolTable.enterScope();
////		 visit children
//		for (VarDec vd : closure.formalArgList) {
//			String vdType = (String) vd.visit(this, arg);
//			argTypes.add(vdType);
//		}
//		for (Statement s : closure.statementList) {
//			s.visit(this, arg);
//		}
//		int numScopesExit = symbolTable.leaveScope();
//		check(numScopesExit > 0 && numScopesExit == numScopes,
//				"unbalanced scopes", closure);
//		closure.setJVMType(JVMType);
//		closure.setArgTypes(argTypes);
//		closure.setResultType(resultType);
//		return resultType;
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Make sure that the name has not already been declared and insert in
	 * symbol table. Visit the closure
	 * @throws Exception 
	 */
	@Override
	public Object visitClosureDec(ClosureDec closureDec, Object arg) throws Exception {
		String ident = closureDec.identToken.getText();
		check(symbolTable.insert(ident, closureDec) == true, "redeclared Clousere", closureDec);
		closureDec.closure.visit(this, arg);
//		return null;
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Check that the given name is declared as a closure Check the argument
	 * types The type is the return type of the closure
	 */
	@Override
	public Object visitClosureEvalExpression(
			ClosureEvalExpression closureEvalExpression, Object arg)
			throws Exception {
		String ident = closureEvalExpression.identToken.getText();
		Declaration dec = symbolTable.lookup(ident);
		check(dec != null, "redeclare ClosureEval", closureEvalExpression);
//		Declaration dec = symbolTable.lookup(ident);
		if (dec instanceof ClosureDec) {
			for (Expression expr : closureEvalExpression.expressionList) {
				expr.visit(this, arg);
			}
		} else {
			throw new TypeCheckException(ident + " is not defined as a closure", closureEvalExpression);
		}		
//		return null;
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitClosureExpression(ClosureExpression closureExpression,
			Object arg) throws Exception {
//		String closureReturnType = (String) closureExpression.closure.visit(this, arg);	
//		return closureReturnType;
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitExpressionLValue(ExpressionLValue expressionLValue,
			Object arg) throws Exception {
		String ident = expressionLValue.identToken.getText();
		Declaration dec = symbolTable.lookup(ident);
		check(dec != null, "redeclare ExpressionLValue", expressionLValue);
//		Declaration dec = symbolTable.lookup(ident);
		if (!(dec instanceof VarDec)) {
			throw new TypeCheckException(ident + " is not defined as a variable", expressionLValue);
		} else {
			String varType = (String) ((VarDec)dec).type.visit(this, arg);			
			if (varType.substring(0, varType.indexOf("<")).equals("Ljava/util/List")) {
				String exprType = (String) expressionLValue.expression.visit(this, arg);
				check(exprType.equals(intType), "List subscript must be int", expressionLValue);
				String elementType = varType.substring(varType.indexOf("<") + 1, varType.lastIndexOf(">"));
				expressionLValue.setType(elementType);
				return elementType;
			} else {
				throw new UnsupportedOperationException("map not yet implemented");
			}
		}
	}

	@Override
	public Object visitExpressionStatement(
			ExpressionStatement expressionStatement, Object arg)
			throws Exception {
		expressionStatement.expression.visit(this, arg);
		return null;
	}

	/**
	 * Check that name has been declared in scope Get its type from the
	 * declaration.
	 * 
	 */
	@Override
	public Object visitIdentExpression(IdentExpression identExpression,
			Object arg) throws Exception {
		String ident = identExpression.identToken.getText();
		Declaration dec = symbolTable.lookup(ident);
		check(dec != null, "undeclare IdentExpression", identExpression);
//		Declaration dec = symbolTable.lookup(ident);
		if (dec instanceof VarDec) {
			VarDec vd = (VarDec) dec;
			String identType = (String) vd.type.visit(this, arg);
			identExpression.setType(identType);
			return identType;
		} else {
			throw new TypeCheckException(ident + " is not defined as a variable", identExpression);
		}		
	}

	@Override
	public Object visitIdentLValue(IdentLValue identLValue, Object arg)
			throws Exception {
		String ident = identLValue.identToken.getText();
		Declaration dec = symbolTable.lookup(ident);
		check(dec != null, "undeclare IdentExpression", identLValue);
//		Declaration dec = symbolTable.lookup(ident);
		if (dec instanceof VarDec) {
			VarDec vd = (VarDec)dec;
			String lvType = (String) vd.type.visit(this, arg);
			identLValue.setType(lvType);
			return lvType;
		} else {
			throw new TypeCheckException(ident + " is not defined as a variable", identLValue);
		}		
	}

	@Override
	public Object visitIfElseStatement(IfElseStatement ifElseStatement,
			Object arg) throws Exception {
		String condType = (String) ifElseStatement.expression.visit(this, arg);
		check(condType.equals(booleanType), "uncompatible IfElse condition", ifElseStatement);
		ifElseStatement.ifBlock.visit(this, arg);
		ifElseStatement.elseBlock.visit(this, arg);
		return null;
	}

	/**
	 * expression type is boolean
	 */
	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg)
			throws Exception {
		String condType = (String) ifStatement.expression.visit(this, arg);
		check(condType.equals(booleanType), "uncompatible If condition", ifStatement);
		ifStatement.block.visit(this, arg);
		return null;
	}

	/**
	 * expression type is int
	 */
	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression,
			Object arg) throws Exception {
		intLitExpression.setType(intType);
		return intType;
	}

	@Override
	public Object visitKeyExpression(KeyExpression keyExpression, Object arg)
			throws Exception {
		String keyExprType = (String) keyExpression.expression.visit(this, arg);
		keyExpression.setType(keyExprType);
		return keyExprType;
	}

	@Override
	public Object visitKeyValueExpression(
			KeyValueExpression keyValueExpression, Object arg) throws Exception {
	//	String keyType = (String) keyValueExpression.key.visit(this, arg);
	//	String valueTyoe = (String) keyValueExpression.value.visit(this, arg);
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitKeyValueType(KeyValueType keyValueType, Object arg)
			throws Exception {
		keyValueType.keyType.visit(this, arg);
		keyValueType.valueType.visit(this, arg);
//		return keyValueType.getJVMType();
		throw new UnsupportedOperationException("not yet implemented");
	}

	// visit the expressions (children) and ensure they are the same type
	// the return type is "Ljava/util/ArrayList<"+type0+">;" where type0 is the
	// type of elements in the list
	// this should handle lists of lists, and empty list. An empty list is
	// indicated by "Ljava/util/ArrayList;".
	@Override
	public Object visitListExpression(ListExpression listExpression, Object arg)
			throws Exception {
		if (listExpression.expressionList.isEmpty()) {	
			listExpression.setType(emptyList);
			return emptyList;
		}		
		String oldListType = (String) listExpression.expressionList.get(0).visit(this, arg);
		for(Expression expr : listExpression.expressionList) {
			String listType = (String) expr.visit(this, arg);
			check(oldListType.equals(listType),	"uncompatible list type", listExpression);
			oldListType = listType;			
		}
		String listType = "Ljava/util/ArrayList<" + oldListType + ">;";
		listExpression.setType(listType);
		return listType;
	}

	/** gets the type from the enclosed expression */
	@Override
	public Object visitListOrMapElemExpression(
			ListOrMapElemExpression listOrMapElemExpression, Object arg)
			throws Exception {
		String ident = listOrMapElemExpression.identToken.getText();
		Declaration dec = symbolTable.lookup(ident);
		check(dec != null, "undeclare MapElemExpression", listOrMapElemExpression);
		if (!(dec instanceof VarDec)) {			
			throw new TypeCheckException(ident + " is not defined as a variable", listOrMapElemExpression);
		}	
		String varType = (String) ((VarDec)dec).type.visit(this, arg);
		if (varType.substring(0, varType.indexOf("<")).equals("Ljava/util/List")) {
			String lomrExprType = (String) listOrMapElemExpression.expression.visit(this, arg);
			check(lomrExprType.equals(intType), "List subscript must be int", listOrMapElemExpression);
			String elementType = varType.substring(varType.indexOf("<") + 1, varType.lastIndexOf(">"));
			listOrMapElemExpression.setType(elementType);
			return elementType;
		} else {
			throw new UnsupportedOperationException("not yet implemented");
		}
	}

	@Override
	public Object visitListType(ListType listType, Object arg) throws Exception {
		listType.type.visit(this, arg);
		return listType.getJVMType();	
	}

	@Override
	public Object visitMapListExpression(MapListExpression mapListExpression,
			Object arg) throws Exception {
		if (mapListExpression.mapList.isEmpty()) {
			return emptyMap;
		}
		String oldMapListType = (String) mapListExpression.mapList.get(0).visit(this, arg);
		for (KeyValueExpression kvExpr : mapListExpression.mapList) {
			String mapListType = (String) kvExpr.visit(this, arg);
			check(oldMapListType.equals(mapListType),	"uncompatible list type", mapListExpression);
			oldMapListType = mapListType;		
		}
//		return oldMapListType;
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitPrintStatement(PrintStatement printStatement, Object arg)
			throws Exception {
		printStatement.expression.visit(this, null);
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		if (arg == null) {
			program.JVMName = program.name;
		} else {
			program.JVMName = arg + "/" + program.name;
		}
		// ignore the import statement
		if (!symbolTable.insert(program.name, null)) {
			throw new TypeCheckException("name already in symbol table",
					program);
		}
		program.block.visit(this, true);
		return null;
	}

	@Override
	public Object visitQualifiedName(QualifiedName qualifiedName, Object arg) {
//		return null;
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * Checks that both expressions have type int.
	 * 
	 * Note that in spite of the name, this is not in the Expression type
	 * hierarchy.
	 */
	@Override
	public Object visitRangeExpression(RangeExpression rangeExpression,
			Object arg) throws Exception {
		String lowerType = (String) rangeExpression.lower.visit(this, arg);
		String upperType = (String) rangeExpression.upper.visit(this, arg);
		check(lowerType.equals(intType) && upperType.equals(intType), "uncompatible range expression", rangeExpression);
		return null;
	}

	// nothing to do here
	@Override
	public Object visitReturnStatement(ReturnStatement returnStatement,
			Object arg) throws Exception {
//		return returnStatement.expression.visit(this, arg);
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitSimpleType(SimpleType simpleType, Object arg)
			throws Exception {
		return simpleType.getJVMType();
	}

	@Override
	public Object visitSizeExpression(SizeExpression sizeExpression, Object arg)
			throws Exception {
		String exprType = (String) sizeExpression.expression.visit(this, arg);		
		if (exprType.substring(0, exprType.indexOf("<")).equals("Ljava/util/List")) {
			sizeExpression.setType(intType);
			return intType;
		} else {
			throw new TypeCheckException("size is undefined for " + exprType, sizeExpression);
		}		
	}

	@Override
	public Object visitStringLitExpression(
			StringLitExpression stringLitExpression, Object arg)
			throws Exception {
		stringLitExpression.setType(stringType);
		return stringType;
	}

	/**
	 * if ! and boolean, then boolean else if - and int, then int else error
	 */
	@Override
	public Object visitUnaryExpression(UnaryExpression unaryExpression,
			Object arg) throws Exception {
		String exprType = (String) unaryExpression.expression.visit(this, arg);
		if(unaryExpression.op.kind == NOT) {
			if(!exprType.equals(booleanType)) {
				throw new TypeCheckException("not operator is undefined for " + exprType, unaryExpression);
			}			
		} else if (unaryExpression.op.kind == MINUS) { 
			if (!exprType.equals(intType)){
				throw new TypeCheckException("minus operator is undefined for " + exprType, unaryExpression);
			}
		} else {			
			throw new TypeCheckException("uncompatible unary expression", unaryExpression);
		}		
		unaryExpression.setType(exprType);
		return exprType;
	}

	@Override
	public Object visitUndeclaredType(UndeclaredType undeclaredType, Object arg)
			throws Exception {
		throw new UnsupportedOperationException(
				"undeclared types not supported");
	}

	@Override
	public Object visitValueExpression(ValueExpression valueExpression,
			Object arg) throws Exception {
//		return valueExpression.expression.visit(this, arg);
		throw new UnsupportedOperationException("not yet implemented");
	}

	/**
	 * check that this variable has not already been declared in the same scope.
	 */
	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws Exception {
		String ident = varDec.identToken.getText();
		check(symbolTable.insert(ident, varDec) == true, "redeclare VarDec", varDec);
		return null;
	}

	/**
	 * All checking will be done in the children since grammar ensures that the
	 * rangeExpression is a rangeExpression.
	 */
	@Override
	public Object visitWhileRangeStatement(
			WhileRangeStatement whileRangeStatement, Object arg)
			throws Exception {
		whileRangeStatement.rangeExpression.visit(this, arg);
		whileRangeStatement.block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitWhileStarStatement(
			WhileStarStatement whileStarStatement, Object arg) throws Exception {
		whileStarStatement.expression.visit(this, arg);
		whileStarStatement.block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg)
			throws Exception {
		whileStatement.expression.visit(this, arg);
		String condType = (String) whileStatement.expression.visit(this, arg);
		check(condType.equals(booleanType), "uncompatible If condition", whileStatement);
		whileStatement.block.visit(this, arg);
		return null;
	}

}
