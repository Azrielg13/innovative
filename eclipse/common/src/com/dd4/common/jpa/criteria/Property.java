package com.dd4.common.jpa.criteria;

import java.util.Collection;


/**
 * A factory for property-specific criterion and projection instances
 * 
 */
public class Property extends PropertyProjection {

	protected Property(String propertyName) {
		super(propertyName);
	}

	public Criterion between(Object min, Object max) {
		return Restrictions.between(getPropertyName(), min, max);
	}

	public Criterion in(Collection<?> values) {
		return Restrictions.in(getPropertyName(), values);
	}

	public Criterion in(Object[] values) {
		return Restrictions.in(getPropertyName(), values);
	}

	public AbstractPropertyBasedCriterion like(String value) {
		return Restrictions.like(getPropertyName(), value);
	}

	public AbstractPropertyBasedCriterion like(String value, MatchMode matchMode) {
		return Restrictions.like(getPropertyName(), value, matchMode);
	}

	public AbstractPropertyBasedCriterion eq(Object value) {
		return Restrictions.eq(getPropertyName(), value);
	}

	public AbstractPropertyBasedCriterion ne(Object value) {
		return Restrictions.ne(getPropertyName(), value);
	}

	public AbstractPropertyBasedCriterion gt(Object value) {
		return Restrictions.gt(getPropertyName(), value);
	}

	public AbstractPropertyBasedCriterion lt(Object value) {
		return Restrictions.lt(getPropertyName(), value);
	}

	public AbstractPropertyBasedCriterion le(Object value) {
		return Restrictions.le(getPropertyName(), value);
	}

	public AbstractPropertyBasedCriterion ge(Object value) {
		return Restrictions.ge(getPropertyName(), value);
	}

	public PropertyExpression eqProperty(Property other) {
		return Restrictions.eqProperty(getPropertyName(), other.getPropertyName());
	}

	public PropertyExpression neProperty(Property other) {
		return Restrictions.neProperty(getPropertyName(), other.getPropertyName());
	}

	public PropertyExpression leProperty(Property other) {
		return Restrictions.leProperty(getPropertyName(), other.getPropertyName());
	}

	public PropertyExpression geProperty(Property other) {
		return Restrictions.geProperty(getPropertyName(), other.getPropertyName());
	}

	public PropertyExpression ltProperty(Property other) {
		return Restrictions.ltProperty(getPropertyName(), other.getPropertyName());
	}

	public PropertyExpression gtProperty(Property other) {
		return Restrictions.gtProperty(getPropertyName(), other.getPropertyName());
	}

	public PropertyExpression eqProperty(String other) {
		return Restrictions.eqProperty(getPropertyName(), other);
	}

	public PropertyExpression neProperty(String other) {
		return Restrictions.neProperty(getPropertyName(), other);
	}

	public PropertyExpression leProperty(String other) {
		return Restrictions.leProperty(getPropertyName(), other);
	}

	public PropertyExpression geProperty(String other) {
		return Restrictions.geProperty(getPropertyName(), other);
	}

	public PropertyExpression ltProperty(String other) {
		return Restrictions.ltProperty(getPropertyName(), other);
	}

	public PropertyExpression gtProperty(String other) {
		return Restrictions.gtProperty(getPropertyName(), other);
	}

	public Criterion isNull() {
		return Restrictions.isNull(getPropertyName());
	}

	public Criterion isNotNull() {
		return Restrictions.isNotNull(getPropertyName());
	}

	public Criterion isEmpty() {
		return Restrictions.isEmpty(getPropertyName());
	}

	public Criterion isNotEmpty() {
		return Restrictions.isNotEmpty(getPropertyName());
	}

	public CountProjection count() {
		return Projections.count(getPropertyName());
	}

	public AggregateProjection max() {
		return Projections.max(getPropertyName());
	}

	public AggregateProjection min() {
		return Projections.min(getPropertyName());
	}

	public AggregateProjection avg() {
		return Projections.avg(getPropertyName());
	}

	public PropertyProjection project() {
		return Projections.property(getPropertyName());
	}

	public PropertyProjection group() {
		return Projections.groupProperty(getPropertyName());
	}

	public Order asc() {
		return Order.asc(getPropertyName());
	}

	public Order desc() {
		return Order.desc(getPropertyName());
	}

	public static Property forName(String propertyName) {
		return new Property(propertyName);
	}

	/**
	 * Get a component attribute of this property
	 */
	public Property getProperty(String propertyName) {
		return forName(getPropertyName() + '.' + propertyName);
	}

	public Criterion eq(CriteriaQuery subselect) {
		return Subqueries.propertyEq(getPropertyName(), subselect);
	}

	public Criterion ne(CriteriaQuery subselect) {
		return Subqueries.propertyNe(getPropertyName(), subselect);
	}

	public Criterion lt(CriteriaQuery subselect) {
		return Subqueries.propertyLt(getPropertyName(), subselect);
	}

	public Criterion le(CriteriaQuery subselect) {
		return Subqueries.propertyLe(getPropertyName(), subselect);
	}

	public Criterion gt(CriteriaQuery subselect) {
		return Subqueries.propertyGt(getPropertyName(), subselect);
	}

	public Criterion ge(CriteriaQuery subselect) {
		return Subqueries.propertyGe(getPropertyName(), subselect);
	}

	public Criterion notIn(CriteriaQuery subselect) {
		return Subqueries.propertyNotIn(getPropertyName(), subselect);
	}

	public Criterion in(CriteriaQuery subselect) {
		return Subqueries.propertyIn(getPropertyName(), subselect);
	}

	public Criterion eqAll(CriteriaQuery subselect) {
		return Subqueries.propertyEqAll(getPropertyName(), subselect);
	}

	public Criterion gtAll(CriteriaQuery subselect) {
		return Subqueries.propertyGtAll(getPropertyName(), subselect);
	}

	public Criterion ltAll(CriteriaQuery subselect) {
		return Subqueries.propertyLtAll(getPropertyName(), subselect);
	}

	public Criterion leAll(CriteriaQuery subselect) {
		return Subqueries.propertyLeAll(getPropertyName(), subselect);
	}

	public Criterion geAll(CriteriaQuery subselect) {
		return Subqueries.propertyGeAll(getPropertyName(), subselect);
	}

	public Criterion gtSome(CriteriaQuery subselect) {
		return Subqueries.propertyGtSome(getPropertyName(), subselect);
	}

	public Criterion ltSome(CriteriaQuery subselect) {
		return Subqueries.propertyLtSome(getPropertyName(), subselect);
	}

	public Criterion leSome(CriteriaQuery subselect) {
		return Subqueries.propertyLeSome(getPropertyName(), subselect);
	}

	public Criterion geSome(CriteriaQuery subselect) {
		return Subqueries.propertyGeSome(getPropertyName(), subselect);
	}

}
