package com.dd4.common.jpa.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;


/**
 * Implementation of the <tt>CriteriaQuery</tt> interface
 * 
 */
public class CriteriaQueryImpl<T> implements CriteriaQuery<T>, javax.persistence.criteria.CriteriaQuery<T>, Serializable {

	public static final String ROOT_ALIAS = "o";

	private final Class<?> entityClass;
	private final String rootAlias;

	SelectClause selectClause = new SelectClause();
	FromClause fromClause = new FromClause();
	private WhereClause whereClause = new WhereClause();
	private GroupByClause groupByClause = new GroupByClause(selectClause);
	private OrderClause orderClause = new OrderClause();

	private CriteriaQuery projectionCriteria;

	private QueryValueTranslator queryValueTranslator = new JpaQueryValueTranslator();

	private List<CriteriaQuery> subCriterias;

	private CriteriaQuery parentCriteria;

	public CriteriaQueryImpl(Class<?> entityClass) {
		this(entityClass, ROOT_ALIAS);
	}

	public CriteriaQueryImpl(Class<?> entityClass, String rootAlias) {
		this(entityClass, rootAlias, null);
	}

	public CriteriaQueryImpl(Class<?> entityClass, String rootAlias, CriteriaQuery parentCriteria) {

		this.entityClass = entityClass;
		this.rootAlias = rootAlias;
		setParentCriteria(parentCriteria);

		subCriterias = new ArrayList<CriteriaQuery>();
	}

	public String toString() {
		return toJpql();
	}

	@Override
	public Query createQuery(EntityManager entityManager) {

		String jpql = toString();
		Query query = entityManager.createQuery(jpql);

		setQueryParameters(query);

		return query;
	}

	public void setQueryParameters(Query query) {

		Map<String, Object> parametricValues = getQueryParameters();

		for (CriteriaQuery subCriteria : subCriterias) {
			parametricValues.putAll(subCriteria.getQueryParameters());
		}

		for (Entry<String, Object> parametricValue : parametricValues.entrySet()) {
			query.setParameter(parametricValue.getKey(), parametricValue.getValue());
		}

	}

	public Map<String, Object> getQueryParameters() {
		return queryValueTranslator.getParametricValues();
	}

	@Override
	public void addSubCriteria(CriteriaQuery subCriteria) {
		subCriterias.add(subCriteria);
	}

	private String toJpql() {
		return selectClause.toJpql(this) + fromClause.toJpql(this) + whereClause.toJpql(this)
				+ groupByClause.toJpql(this) + orderClause.toJpql(this);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public CriteriaQuery getParentCriteria() {
		return parentCriteria;
	}

	public void setParentCriteria(CriteriaQuery parentCriteria) {
		
		this.parentCriteria = parentCriteria;

		if (parentCriteria != null) {
			parentCriteria.addSubCriteria(this);
		}
	}

	public CriteriaQuery getProjectionCriteria() {
		return projectionCriteria;
	}

	public CriteriaQuery add(CriteriaQuery criteriaInst, Criterion expression) {
		whereClause.add(new CriterionEntry(expression, criteriaInst));
		return this;
	}

	// CriteriaQuery impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public String getAliasedPropertyName(String propertyName) {

		if (isAliased(propertyName)) {
			return propertyName;
		}

		return getAlias() + "." + propertyName;
	}

	private boolean isAliased(String propertyName) {

		String[] chain = propertyName.split("\\.");
		if (chain.length > 1) {
			propertyName = chain[0];
		}

		List<String> allAliases = getIncludedAliases();
		if (allAliases.contains(propertyName)) {
			return true;
		}

		return false;
	}

	@Override
	public List<String> getIncludedAliases() {

		List<String> allAliases = new ArrayList<String>();

		allAliases.add(getAlias());
		allAliases.addAll(selectClause.getProjectionAliases());
		allAliases.addAll(fromClause.getJoinAliases());

		if(parentCriteria != null) {
			allAliases.addAll(parentCriteria.getIncludedAliases());
		}
		
		return allAliases;
	}

	public String getAlias() {
		return rootAlias;
	}

	public CriteriaQuery addProjection(Projection projection) {

		selectClause.add(projection);
		projectionCriteria = this;
		return this;
	}

	public CriteriaQueryImpl<T> addRestriction(Criterion expression) {
		add(this, expression);
		return this;
	}

	public CriteriaQuery addOrder(Order ordering) {
		orderClause.add(new OrderEntry(ordering, this));
		return this;
	}

	public CriteriaQuery addJoin(Join join) {
		fromClause.add(join);
		return this;
	}

	public CriteriaQuery createAlias(String associationPath, String alias) {
		return createAlias(associationPath, alias);
	}

	@Override
	public String getJpaValue(Object value) {
		return queryValueTranslator.translateValue(value);
	}

	public ProjectionList clearProjections() {
		return selectClause.clearProjections();
	}

	public static final class CriterionEntry implements Serializable {

		private final Criterion criterion;
		private final CriteriaQuery criteriaQuery;

		private CriterionEntry(Criterion criterion, CriteriaQuery criteriaQuery) {
			this.criteriaQuery = criteriaQuery;
			this.criterion = criterion;
		}

		public Criterion getCriterion() {
			return criterion;
		}

		public CriteriaQuery getCriteria() {
			return criteriaQuery;
		}

		public String toString() {
			return criterion.toJpql(criteriaQuery);
		}
	}

	public static final class OrderEntry implements Serializable {

		private final Order order;
		private final CriteriaQuery criteriaQuery;

		private OrderEntry(Order order, CriteriaQuery criteriaQuery) {
			this.criteriaQuery = criteriaQuery;
			this.order = order;
		}

		public Order getOrder() {
			return order;
		}

		public CriteriaQuery getCriteria() {
			return criteriaQuery;
		}

		public String toString() {
			return order.toJpql(criteriaQuery);
		}
	}

	public interface JpqlClause {

		String toJpql(CriteriaQuery criteriaQuery);
	}

	public static class WhereClause implements JpqlClause {

		private List<CriterionEntry> criterionEntries = new ArrayList<CriterionEntry>();

		public String toJpql(CriteriaQuery criteriaQuery) {

			StringBuilder sb = new StringBuilder();

			if (!criterionEntries.isEmpty()) {
				int size = criterionEntries.size();
				for (int i = 0; i < size; i++) {
					sb.append(criterionEntries.get(i));
					if (i != (size - 1)) {
						sb.append(" AND ");
					}
				}
				sb.insert(0, " WHERE ");
			}

			return sb.toString();
		}

		public void add(CriterionEntry criterionEntry) {
			criterionEntries.add(criterionEntry);
		}

	}

	public static class OrderClause implements JpqlClause {

		private List<OrderEntry> orderEntries = new ArrayList<OrderEntry>();

		public String toJpql(CriteriaQuery criteriaQuery) {

			StringBuilder sb = new StringBuilder();

			if (!orderEntries.isEmpty()) {
				int size = orderEntries.size();
				for (int i = 0; i < size; i++) {
					sb.append(orderEntries.get(i));
					if (i != (size - 1)) {
						sb.append(", ");
					}
				}
				sb.insert(0, " ORDER BY ");
			}

			return sb.toString();

		}

		public void add(OrderEntry orderEntry) {
			orderEntries.add(orderEntry);
		}

	}

	public static class SelectClause implements JpqlClause {

		private ProjectionList projectionList = Projections.projectionList();
		private List<String> projectionAliases = new ArrayList<String>();

		public String toJpql(CriteriaQuery criteriaQuery) {

			StringBuilder sb = new StringBuilder();

			int size = projectionList.getLength();

			if (size == 0) {
				sb.append(criteriaQuery.getAlias());
			} else {
				for (int i = 0; i < size; i++) {
					sb.append(projectionList.getProjection(i).toJpql(criteriaQuery, i));
					if (i != (size - 1)) {
						sb.append(", ");
					}
				}
			}
			sb.insert(0, "SELECT ");

			return sb.toString();
		}

		public void add(Projection projection) {

			projectionList.add(projection);
			insertAliasesToAliasList();
		}

		private void insertAliasesToAliasList() {

			String[] aliases = projectionList.getAliases();
			for (String alias : aliases) {
				if (alias != null) {
					projectionAliases.add(alias);
				}
			}
		}

		public List<String> getProjectionAliases() {
			return projectionAliases;
		}

		public ProjectionList clearProjections() {

			ProjectionList oldProjectionList = projectionList;

			projectionList = Projections.projectionList();
			projectionAliases.clear();

			return oldProjectionList;
		}

	}

	public static class GroupByClause implements JpqlClause {

		private final SelectClause selectClause;

		public GroupByClause(SelectClause selectClause) {
			this.selectClause = selectClause;
		}

		public String toJpql(CriteriaQuery criteriaQuery) {

			if (selectClause.projectionList.isGrouped()) {
				return " GROUP BY " + selectClause.projectionList.toGroupJpql(criteriaQuery);
			}

			return "";
		}
	}

	public static class FromClause implements JpqlClause {

		private List<Join> joinList = new ArrayList<Join>();
		private List<String> joinAliases = new ArrayList<String>();

		public String toJpql(CriteriaQuery criteriaQuery) {

			StringBuilder sb = new StringBuilder();

			sb.append(" FROM " + criteriaQuery.getEntityClass().getSimpleName() + " " + criteriaQuery.getAlias());
			for (Join join : joinList) {
				sb.append(" " + join.toJpql(criteriaQuery));
			}

			return sb.toString();
		}

		public void add(Join join) {

			joinList.add(join);
			if (join.getAlias() != null) {
				joinAliases.add(join.getAlias());
			}
		}

		public List<String> getJoinAliases() {
			return joinAliases;
		}
	}

	@Override
	public <X> Root<X> from(Class<X> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> Root<X> from(EntityType<X> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Expression<?>> getGroupList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate getGroupRestriction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate getRestriction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<T> getResultType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Root<?>> getRoots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Selection<T> getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDistinct() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <U> Subquery<U> subquery(Class<U> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> distinct(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<javax.persistence.criteria.Order> getOrderList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ParameterExpression<?>> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> groupBy(
			Expression<?>... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> groupBy(
			List<Expression<?>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> having(
			Expression<Boolean> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> having(Predicate... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> multiselect(
			Selection<?>... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> multiselect(
			List<Selection<?>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> orderBy(
			javax.persistence.criteria.Order... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> orderBy(
			List<javax.persistence.criteria.Order> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> select(
			Selection<? extends T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> where(
			Expression<Boolean> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.persistence.criteria.CriteriaQuery<T> where(Predicate... p) {
		
		return null;
	}

}
