package cop5555sp15.ast;

import java.util.ArrayList;
import java.util.List;

import cop5555sp15.TypeConstants;
import cop5555sp15.symbolTable.SymbolTable;
import static cop5555sp15.TokenStream.Kind.*;

public class TypeCheckVisitor implements ASTVisitor, TypeConstants {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		ASTNode node;

		public TypeCheckException(String message, ASTNode node) {
			super(node.firstToken.lineNumber + ":" + message);
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
		check(lvType.equals(exprType), "uncompatible assignment scopes", assignmentStatement);
		return null;		
	}

	/**
	 * Ensure that both types are the same, save and return the result type
	 */
	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression,
			Object arg) throws Exception {
		String expr0Type = (String) binaryExpression.expression0.visit(this,arg);
		String expr1Type = (String) binaryExpression.expression1.visit(this,arg);
		check(expr0Type.equals(expr1Type), "uncompatible bianry expression", binaryExpression);
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
		List<String> argTypes = new ArrayList<String>();
		String resultType = null;
		int numScopes = symbolTable.enterScope();
		// visit children
		for (VarDec vd : closure.formalArgList) {
			String vdType = (String) vd.visit(this, arg);
			argTypes.add(vdType);
		}
		for (Statement s : closure.statementList) {
			s.visit(this, arg);
		}
		int numScopesExit = symbolTable.leaveScope();
		check(numScopesExit > 0 && numScopesExit == numScopes,
				"unbalanced scopes", closure);
//		closure.setJVMType(JVMType);
		closure.setArgTypes(argTypes);
//		closure.setResultType(resultType);
		return resultType;
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
		String closureType = (String) closureDec.closure.visit(this, arg);
		return closureType;
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
		check(symbolTable.insert(ident, null) == false, "redeclare ClosureEval", closureEvalExpression);
		ClosureDec cloDec = (ClosureDec) symbolTable.lookup(ident);
		for (Expression expr : closureEvalExpression.expressionList) {
			String exprType = (String) expr.visit(this, arg);
		}
		return cloDec.visit(this, arg);
	}

	@Override
	public Object visitClosureExpression(ClosureExpression closureExpression,
			Object arg) throws Exception {
		return closureExpression.closure.visit(this, arg);	
	}

	@Override
	public Object visitExpressionLValue(ExpressionLValue expressionLValue,
			Object arg) throws Exception {
		String ident = expressionLValue.identToken.getText();
		check(symbolTable.insert(ident, null) == false, "redeclare ExpressionLValue", expressionLValue);
		Declaration dec = symbolTable.lookup(ident);
		String exprLvType = (String) dec.visit(this, arg);
		return exprLvType;
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
		check(symbolTable.insert(ident, null) == false, "undeclare IdentExpression", identExpression);
		Declaration dec = symbolTable.lookup(ident);
		String identExprType = (String) dec.visit(this, arg);
		return identExprType;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identLValue, Object arg)
			throws Exception {
		String ident = identLValue.identToken.getText();
		check(symbolTable.insert(ident, null) == false, "undeclare IdentExpression", identLValue);
		Declaration dec = symbolTable.lookup(ident);
		String identLvType = (String) dec.visit(this, arg);
		return identLvType;				
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
		String keyType = (String) keyValueExpression.key.visit(this, arg);
		String valueTyoe = (String) keyValueExpression.value.visit(this, arg);
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public Object visitKeyValueType(KeyValueType keyValueType, Object arg)
			throws Exception {
		keyValueType.keyType.visit(this, arg);
		keyValueType.valueType.visit(this, arg);
		return keyValueType.getJVMType();
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
			return emptyList;
		}
		String oldListType = (String) listExpression.expressionList.get(0).visit(this, arg);
		for(Expression expr : listExpression.expressionList) {
			String listType = (String) expr.visit(this, arg);
			check(oldListType.equals(listType),	"uncompatible list type", listExpression);
			oldListType = listType;			
		}
		return oldListType;
	}

	/** gets the type from the enclosed expression */
	@Override
	public Object visitListOrMapElemExpression(
			ListOrMapElemExpression listOrMapElemExpression, Object arg)
			throws Exception {
		String ident = listOrMapElemExpression.identToken.getText();
		check(symbolTable.insert(ident, null) == false, "undeclare MapElemExpression", listOrMapElemExpression);
		String lomrExprType = (String) listOrMapElemExpression.expression.visit(this, arg);
		return lomrExprType;
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
		return oldMapListType;
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
		assert false;
		return null;
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
		return returnStatement.expression.visit(this, arg);
	}

	@Override
	public Object visitSimpleType(SimpleType simpleType, Object arg)
			throws Exception {
		return simpleType.getJVMType();
	}

	@Override
	public Object visitSizeExpression(SizeExpression sizeExpression, Object arg)
			throws Exception {
		return sizeExpression.expression.visit(this, arg);		
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
		boolean isUnary = unaryExpression.op.kind == NOT &&  exprType.equals(booleanType);
		isUnary |= unaryExpression.op.kind == MINUS && exprType.equals(intType);
		check(isUnary, "uncompatible unary expression", unaryExpression);
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
		return valueExpression.expression.visit(this, arg);
	}

	/**
	 * check that this variable has not already been declared in the same scope.
	 */
	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws Exception {
		String ident = varDec.identToken.getText();
		check(symbolTable.insert(ident, varDec) == true, "redeclare VarDec", varDec);
		String varType = (String) varDec.type.visit(this, arg);
		return varType;
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
		whileStatement.block.visit(this, arg);
		return null;
	}

}
