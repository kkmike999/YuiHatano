package android.database.sqlite;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.io.StringReader;
import java.util.List;

/**
 * Created by kkmike999 on 2017/06/05.
 * <p>
 * sql语句解析工具类
 */
public class KbSqlParser {

    public static String bindArgs(String sql, Object[] bindArgs) {
        CCJSqlParserManager pm = new CCJSqlParserManager();

        try {
            Statement statement = pm.parse(new StringReader(sql));

            if (statement instanceof Select) {
                Select      select = (Select) statement;
                PlainSelect body   = (PlainSelect) select.getSelectBody();
                Expression  where  = body.getWhere();

                replaceExpression(where, bindArgs, 0);

                return body.toString();
            }
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        return sql;
    }

    protected static int replaceExpression(Expression expression, Object[] args, int position) {
        if (expression instanceof Parenthesis) {
            // 有括号的块
            Parenthesis parenthesis = (Parenthesis) expression;
            return replaceExpression(parenthesis.getExpression(), args, position);
        } else if (expression instanceof EqualsTo) {
            // =
            EqualsTo   equalsTo = (EqualsTo) expression;
            Expression rightExp = equalsTo.getRightExpression();

            if (rightExp instanceof JdbcParameter) {
                Object arg = args[position];

                equalsTo.setRightExpression(parseToValue(arg));

                position++;
            }
            return position;

        } else if (expression instanceof LikeExpression) {
            // LIKE
            LikeExpression likeExp  = (LikeExpression) expression;
            Expression     rightExp = likeExp.getRightExpression();

            if (rightExp instanceof JdbcParameter) {
                Object arg = args[position];

                likeExp.setRightExpression(parseToValue(arg));

                position++;
            }
            return position;

        } else if (expression instanceof Between) {
            // BETWEEN
            Between between = (Between) expression;

            Expression betweenStartExp = between.getBetweenExpressionStart();
            Expression betweenEndExp   = between.getBetweenExpressionEnd();

            if (betweenStartExp instanceof JdbcParameter) {
                Object arg = args[position];

                between.setBetweenExpressionStart(parseToValue(arg));

                position++;
            }
            if (betweenEndExp instanceof JdbcParameter) {
                Object arg = args[position];

                between.setBetweenExpressionEnd(parseToValue(arg));

                position++;
            }
            return position;

        } else if (expression instanceof InExpression) {
            // in (...)
            InExpression inExpression = (InExpression) expression;
            ItemsList    itemsList    = inExpression.getRightItemsList();

            if (itemsList instanceof ExpressionList) {
                ExpressionList   expressionList = (ExpressionList) itemsList;
                List<Expression> exps           = expressionList.getExpressions();

                for (int i = 0; i < exps.size(); i++) {
                    Expression exp = exps.get(i);

                    if (exp instanceof JdbcParameter) {
                        Object arg = args[position];

                        exps.set(i, parseToValue(arg));

                        position++;
                    }
                }
            }
            return position;

        } else if (expression instanceof AndExpression || expression instanceof OrExpression) {
            // AND,OR
            BinaryExpression binaryExpression = (BinaryExpression) expression;

            position = replaceExpression(binaryExpression.getLeftExpression(), args, position);
            position = replaceExpression(binaryExpression.getRightExpression(), args, position);

            return position;
        }

        return position;
    }

    private static Expression parseToValue(Object arg) {
        if (arg instanceof Long || arg instanceof Integer) {
            return new LongValue(arg.toString());
        } else if (arg instanceof Double || arg instanceof Float) {
            return new DoubleValue(arg.toString());
        }

        return new StringValue(arg.toString());
    }
}
