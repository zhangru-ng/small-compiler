package cop5555sp15.ast;

import java.util.Iterator;

public class ToStringVisitor implements ASTVisitor {

	StringBuilder sb;

	public ToStringVisitor() {
		sb = new StringBuilder();
	}

	public String getString() {
		return sb.toString();
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		sb.append("Program\n");
		sb.append("import");
		String indent = "  ";
		Iterator<QualifiedName> it = program.imports.iterator();
		while (it.hasNext()) {
			sb.append('\n').append(indent).append(it.next().name);
		}
		sb.append('\n');
		sb.append("class ").append(program.name).append('\n');
		program.block.visit(this, indent);
		sb.append('\n');
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		sb.append(arg).append("Block").append('\n');
		String indent = arg + "  ";
		for (BlockElem elem : block.elems) {
			elem.visit(this, indent);
		}
		return null;
	}

	@Override
	public Object visitClosure(Closure closure, Object arg) throws Exception {
		sb.append(arg).append("Closure").append('\n');
		String indent = arg + "  ";
		for (VarDec dec : closure.formalArgList) {
			dec.visit(this, indent);
		}
		for (Statement statement : closure.statementList) {
			statement.visit(this, indent);
		}
		return null;
	}

	@Override
	public Object visitClosureDec(ClosureDec closureDeclaration, Object arg)
			throws Exception {
		sb.append(arg).append("ClosureDec").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(closureDeclaration.identToken.getText()).append('\n');;
		closureDeclaration.closure.visit(this, indent);
		return null;
	}

	@Override
	public Object visitSimpleType(SimpleType simpleType, Object arg) {
		sb.append(arg).append("SimpleType").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(simpleType.type.getText()).append('\n');
		return null;
	}

	@Override
	public Object visitKeyValueType(KeyValueType keyValueType, Object arg)
			throws Exception {
		sb.append(arg).append("KeyValueType").append('\n');
		String indent = arg + "  ";
		keyValueType.keyType.visit(this, indent);
		keyValueType.valueType.visit(this, indent);
		return null;
	}

	@Override
	public Object visitListType(ListType listType, Object arg) throws Exception {
		sb.append(arg).append("ListType").append('\n');
		String indent = arg + "  ";
		listType.type.visit(this, indent);
		return null;
	}

	@Override
	public Object visitAssignmentStatement(
			AssignmentStatement assignmentStatement, Object arg)
			throws Exception {
		sb.append(arg).append("AssignmentStatement").append('\n');
		String indent = arg + "  ";
		assignmentStatement.lvalue.visit(this, indent);
		assignmentStatement.expression.visit(this, indent);
		return null;
	}

	@Override
	public Object visitPrintStatement(PrintStatement printStatement, Object arg)
			throws Exception {
		sb.append(arg).append("PrintStatement").append('\n');
		String indent = arg + "  ";
		printStatement.expression.visit(this, indent);
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg)
			throws Exception {
		sb.append(arg).append("WhileStatement").append('\n');
		String indent = arg + "  ";
		whileStatement.expression.visit(this, indent);
		whileStatement.block.visit(this, indent);
		return null;
	}

	@Override
	public Object visitWhileStarStatement(
			WhileStarStatement whileStarStatement, Object arg) throws Exception {
		sb.append(arg).append("WhileStarStatement").append('\n');
		String indent = arg + "  ";
		whileStarStatement.expression.visit(this, indent);
		whileStarStatement.block.visit(this, indent);
		return null;
	}

	@Override
	public Object visitWhileRangeStatement(
			WhileRangeStatement whileRangeStatement, Object arg) throws Exception {
		sb.append(arg).append("WhileRangeStatement").append('\n');
		String indent = arg + "  ";
		whileRangeStatement.rangeExpression.visit(this, indent);
		whileRangeStatement.block.visit(this, indent);
		return null;
	}

	@Override
	public Object visitRangeExpression(RangeExpression rangeExpression,
			Object arg) throws Exception {
		sb.append(arg).append("RangeExpression").append('\n');
		String indent = arg + "  ";
		rangeExpression.lower.visit(this, indent);
		rangeExpression.upper.visit(this, indent);
		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		sb.append(arg).append("IfStatement").append('\n');
		String indent = arg + "  ";
		ifStatement.expression.visit(this, indent);
		ifStatement.block.visit(this, indent);
		return null;
	}

	@Override
	public Object visitIfElseStatement(IfElseStatement ifElseStatement,
			Object arg) throws Exception {
		sb.append(arg).append("IfElseStatement").append('\n');
		String indent = arg + "  ";
		ifElseStatement.expression.visit(this, indent);
		ifElseStatement.ifBlock.visit(this, indent);
		ifElseStatement.elseBlock.visit(this, indent);
		return null;
	}

	@Override
	public Object visitExpressionStatement(
			ExpressionStatement expressionStatement, Object arg) throws Exception {
		sb.append(arg).append("ExpressionStatement").append('\n');
		String indent = arg + "  ";
		expressionStatement.expression.visit(this, indent);
		return null;
	}

	@Override
	public Object visitReturnStatement(ReturnStatement returnStatement,
			Object arg) throws Exception {
		sb.append(arg).append("ReturnStatement").append('\n');
		String indent = arg + "  ";
		returnStatement.expression.visit(this, indent);
		return null;
	}

	@Override
	public Object visitClosureEvalExpression(
			ClosureEvalExpression closureExpression, Object arg) throws Exception {
		sb.append(arg).append("ClosureEvalExpression").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(closureExpression.identToken.getText()).append('\n');
		for (Expression e: closureExpression.expressionList){
			e.visit(this,indent);
		}
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identLValue, Object arg) {
		sb.append(arg).append("IdentLValue").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(identLValue.identToken.getText()).append('\n');
		return null;
	}

	@Override
	public Object visitExpressionLValue(ExpressionLValue expressionLValue,
			Object arg) throws Exception {
		sb.append(arg).append("ExpressionLValue").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(expressionLValue.identToken.getText()).append('\n');
		expressionLValue.expression.visit(this,indent);
		return null;
	}

	@Override
	public Object visitKeyValueExpression(
			KeyValueExpression keyValueExpression, Object arg) throws Exception {
		sb.append(arg).append("KeyValueExpression").append('\n');
		String indent = arg + "  ";
		keyValueExpression.key.visit(this,indent);
		keyValueExpression.value.visit(this,indent);
		return null;
	}


	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression,
			Object arg) throws Exception {
		sb.append(arg).append("BinaryExpression").append('\n');
		String indent = arg + "  ";
		binaryExpression.expression0.visit(this,indent);
		sb.append(indent).append(binaryExpression.op.getText()).append('\n');		
		binaryExpression.expression1.visit(this,indent);	
		return null;
	}

	@Override
	public Object visitListOrMapElemExpression(
			ListOrMapElemExpression listOrMapElemExpression, Object arg) throws Exception {
		sb.append(arg).append("ListOrMapElemExpression").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(listOrMapElemExpression.identToken.getText()).append('\n');	
		listOrMapElemExpression.expression.visit(this,indent);
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression,
			Object arg) {
		sb.append(arg).append("IdentExpression").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(identExpression.identToken.getText()).append('\n');	
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression,
			Object arg) {
		sb.append(arg).append("IntLitExpression").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(intLitExpression.value).append('\n');	
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(
			BooleanLitExpression booleanLitExpression, Object arg) {
		sb.append(arg).append("BooleanLitExpression").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(booleanLitExpression.value).append('\n');	
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnaryExpression unaryExpression,
			Object arg) throws Exception {
		sb.append(arg).append("UnaryExpression").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(unaryExpression.op.getText()).append('\n');		
		unaryExpression.expression.visit(this,indent);	
		return null;
	}

	@Override
	public Object visitStringLitExpression(
			StringLitExpression stringLitExpression, Object arg) {
		sb.append(arg).append("StringLitExpression").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(stringLitExpression.value).append('\n');	
		return null;
	}

	@Override
	public Object visitClosureExpression(ClosureExpression closureExpression,
			Object arg) throws Exception {
		sb.append(arg).append("ClosureExpression").append('\n');
		String indent = arg + "  ";	
		closureExpression.closure.visit(this,indent);	
		return null;
	}

	@Override
	public Object visitListExpression(ListExpression listExpression, Object arg) throws Exception {
		sb.append(arg).append("ListExpression").append('\n');
		String indent = arg + "  ";
		for (Expression e: listExpression.expressionList){
			e.visit(this,indent);
		}
		return null;
	}

	@Override
	public Object visitMapListExpression(MapListExpression mapListExpression,
			Object arg) throws Exception {
		sb.append(arg).append("MapListExpression").append('\n');
		String indent = arg + "  ";
		for (Expression e: mapListExpression.mapList){
			e.visit(this,indent);
		}
		return null;
	}

	@Override
	public Object visitSizeExpression(SizeExpression sizeExpression, Object arg) throws Exception {
		sb.append(arg).append("SizeExpression").append('\n');
		String indent = arg + "  ";	
		sizeExpression.expression.visit(this,indent);	
		return null;
	}

	@Override
	public Object visitKeyExpression(KeyExpression keyExpression, Object arg) throws Exception {
		sb.append(arg).append("KeyExpression").append('\n');
		String indent = arg + "  ";	
		keyExpression.expression.visit(this,indent);	
		return null;
	}

	@Override
	public Object visitValueExpression(ValueExpression valueExpression,
			Object arg) throws Exception {
		sb.append(arg).append("ValueExpression").append('\n');
		String indent = arg + "  ";	
		valueExpression.expression.visit(this,indent);	
		return null;
	}


	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws Exception {
		sb.append(arg).append("VarDec").append('\n');
		String indent = arg + "  ";
		sb.append(indent).append(varDec.identToken.getText()).append('\n');
		varDec.type.visit(this, indent);
		return null;
	}

	@Override
	public Object visitQualifiedName(QualifiedName qualifiedName, Object arg) {
		assert false;
		return null;
	}

}
