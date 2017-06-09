package android.database.sqlite;

import android.support.annotation.Nullable;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
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
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kkmike999 on 2017/06/05.
 * <p>
 * sql语句解析工具类
 */
public class KbSqlParser {

    public static String bindArgs(String sql, @Nullable Object[] bindArgs) {
        if (bindArgs == null || bindArgs.length == 0) {
            return sql;
        }

        try {
            CCJSqlParserManager pm        = new CCJSqlParserManager();
            Statement           statement = pm.parse(new StringReader(sql));

            Set<Expression> expressionSet = findBindArgsExpressions(statement);

            Iterator<Object> iterator = Arrays.asList(bindArgs).iterator();
            bindExpressionArgs(expressionSet, iterator);

            return statement.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

    protected static void bindExpressionArgs(Set<Expression> expressionSet, Iterator<Object> iterator) {
        for (Expression expression : expressionSet) {
            bindExpressionArgs(expression, iterator);
        }
    }

    /**
     * 用数据替换某个表达快（例如，SET、WHERE）的绑定变量
     *
     * @param expression 表达快
     * @param iterator   绑定数据 迭代器
     * @return
     */
    protected static void bindExpressionArgs(Expression expression, Iterator<Object> iterator) {
        if (expression instanceof Parenthesis) {
            // 有括号的块
            Parenthesis parenthesis = (Parenthesis) expression;

            bindExpressionArgs(parenthesis.getExpression(), iterator);

            return;
        } else if (expression instanceof EqualsTo) {
            // =
            EqualsTo   equalsTo = (EqualsTo) expression;
            Expression rightExp = equalsTo.getRightExpression();

            if (rightExp instanceof JdbcParameter) {
                Object arg = iterator.next();

                equalsTo.setRightExpression(parseToValue(arg));
            }
            return;
        } else if (expression instanceof LikeExpression) {
            // LIKE
            LikeExpression likeExp  = (LikeExpression) expression;
            Expression     rightExp = likeExp.getRightExpression();

            if (rightExp instanceof JdbcParameter) {
                Object arg = iterator.next();

                likeExp.setRightExpression(parseToValue(arg));
            }
            return;
        } else if (expression instanceof Between) {
            // BETWEEN
            Between between = (Between) expression;

            Expression betweenStartExp = between.getBetweenExpressionStart();
            Expression betweenEndExp   = between.getBetweenExpressionEnd();

            if (betweenStartExp instanceof JdbcParameter) {
                Object arg = iterator.next();

                between.setBetweenExpressionStart(parseToValue(arg));
            }
            if (betweenEndExp instanceof JdbcParameter) {
                Object arg = iterator.next();

                between.setBetweenExpressionEnd(parseToValue(arg));
            }
            return;

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
                        Object arg = iterator.next();

                        exps.set(i, parseToValue(arg));
                    }
                }
            }
            return;

        } else if (expression instanceof AndExpression || expression instanceof OrExpression) {
            // AND,OR
            BinaryExpression binaryExpression = (BinaryExpression) expression;

            bindExpressionArgs(binaryExpression.getLeftExpression(), iterator);
            bindExpressionArgs(binaryExpression.getRightExpression(), iterator);

            return;
        } else if (expression instanceof ValueExpression) {
            // INSERT ..... VALUES (?,?, ...)
            ValueExpression  valueExpression = (ValueExpression) expression;
            List<Expression> exps            = valueExpression.getExpressions();

            for (int i = 0; i < exps.size(); i++) {
                Expression exp = exps.get(i);

                if (exp instanceof JdbcParameter) {
                    Object arg = iterator.next();

                    exps.set(i, parseToValue(arg));
                }
            }
            return;
        } else if (expression instanceof UpdateSetExpression) {
            UpdateSetExpression updateSetExp = (UpdateSetExpression) expression;
            List<Expression>    exps         = updateSetExp.getExpressions();
            List<Column>        columns      = updateSetExp.getColumns();

            for (int i = 0; i < exps.size(); i++) {
                Expression exp = exps.get(i);

                if (exp instanceof JdbcParameter) {
                    Object arg = iterator.next();

                    exps.set(i, parseToValue(arg));
                }
            }
            return;
        }
    }

    /**
     * 找到所有绑定变量
     *
     * @param sql
     * @return
     */
    protected static Set<Expression> findBindArgsExpressions(String sql) {
        CCJSqlParserManager pm = new CCJSqlParserManager();

        try {
            Statement statement = pm.parse(new StringReader(sql));

            Set<Expression> expressionSet = findBindArgsExpressions(statement);

            return expressionSet;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return new LinkedHashSet<>();
    }

    /**
     * 查找包含'?'的表达块（绑定变量）
     *
     * @param statement
     * @return
     */
    protected static Set<Expression> findBindArgsExpressions(Statement statement) {
        if (statement instanceof Insert) {
            Insert insert = (Insert) statement;

            Set<Expression> expressionSet = new LinkedHashSet<>();

            ExpressionList   itemsList   = (ExpressionList) insert.getItemsList();
            List<Expression> expressions = itemsList.getExpressions();

            for (Expression expression : expressions) {
                if (expression instanceof JdbcParameter) {
                    expressionSet.add(new ValueExpression(expressions));
                    break;
                }
            }

            return expressionSet;
        } else if (statement instanceof Delete) {
            Delete     delete = (Delete) statement;
            Expression where  = delete.getWhere();

            Set<Expression> expressionSet = findBindArgsExpressions(where, new LinkedHashSet<Expression>());

            return expressionSet;

        } else if (statement instanceof Update) {
            Update           update      = (Update) statement;
            Expression       where       = update.getWhere();
            List<Column>     columns     = update.getColumns();
            List<Expression> expressions = update.getExpressions();

            Expression exp = new UpdateSetExpression(columns, expressions);

            // 先添加SET表达式
            Set<Expression> expressionSet = new LinkedHashSet<>();
            expressionSet.add(exp);

            // 再添加where表达式
            expressionSet = findBindArgsExpressions(where, expressionSet);

            return expressionSet;
        } else if (statement instanceof Select) {
            Select      select = (Select) statement;
            PlainSelect body   = (PlainSelect) select.getSelectBody();
            Expression  where  = body.getWhere();

            Set<Expression> expressionSet = findBindArgsExpressions(where, new LinkedHashSet<Expression>());

            return expressionSet;
        }
        return new LinkedHashSet<>();
    }

    /**
     * 查找包含'?'的表达块（绑定变量）
     *
     * @param expression
     * @param expressionSet
     * @return
     */
    private static Set<Expression> findBindArgsExpressions(Expression expression, Set<Expression> expressionSet) {
        if (expression instanceof Parenthesis) {
            // 有括号的块
            Parenthesis parenthesis = (Parenthesis) expression;
            return findBindArgsExpressions(parenthesis.getExpression(), expressionSet);
        } else if (expression instanceof EqualsTo) {
            // =
            EqualsTo   equalsTo = (EqualsTo) expression;
            Expression rightExp = equalsTo.getRightExpression();

            if (rightExp instanceof JdbcParameter) {
                expressionSet.add(equalsTo);
            }
            return expressionSet;
        } else if (expression instanceof LikeExpression) {
            // LIKE
            LikeExpression likeExp  = (LikeExpression) expression;
            Expression     rightExp = likeExp.getRightExpression();

            if (rightExp instanceof JdbcParameter) {
                expressionSet.add(likeExp);
            }
            return expressionSet;

        } else if (expression instanceof Between) {
            // BETWEEN
            Between between = (Between) expression;

            Expression betweenStartExp = between.getBetweenExpressionStart();
            Expression betweenEndExp   = between.getBetweenExpressionEnd();

            if (betweenStartExp instanceof JdbcParameter || betweenEndExp instanceof JdbcParameter) {
                expressionSet.add(between);
            }
            return expressionSet;
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
                        // 只要其中一个是JdbcParameter，把父Expression返回即可
                        expressionSet.add(inExpression);

                        return expressionSet;
                    }
                }
            }
            return expressionSet;
        } else if (expression instanceof AndExpression || expression instanceof OrExpression) {
            // AND,OR
            BinaryExpression binaryExpression = (BinaryExpression) expression;

            expressionSet = findBindArgsExpressions(binaryExpression.getLeftExpression(), expressionSet);
            expressionSet = findBindArgsExpressions(binaryExpression.getRightExpression(), expressionSet);

            return expressionSet;
        }

        return expressionSet;
    }

    private static Expression parseToValue(Object arg) {
        if (arg instanceof Long || arg instanceof Integer) {
            return new LongValue(arg.toString());
        } else if (arg instanceof Double || arg instanceof Float) {
            return new DoubleValue(arg.toString());
        }

        return new StringValue(arg.toString());
    }

    private static class UpdateSetExpression implements Expression {
        List<Column>     columns     = new ArrayList<>();
        List<Expression> expressions = new ArrayList<>();

        public UpdateSetExpression(List<Column> columns, List<Expression> expressions) {
            this.columns = columns;
            this.expressions = expressions;
        }

        public List<Column> getColumns() {
            return columns;
        }

        public List<Expression> getExpressions() {
            return expressions;
        }

        @Override
        public void accept(ExpressionVisitor expressionVisitor) {}
    }

    private static class ValueExpression implements Expression {
        List<Expression> exps = new ArrayList<>();

        public ValueExpression(List<Expression> exps) {
            this.exps = exps;
        }

        public List<Expression> getExpressions() {
            return exps;
        }

        @Override
        public void accept(ExpressionVisitor expressionVisitor) {}
    }
}
