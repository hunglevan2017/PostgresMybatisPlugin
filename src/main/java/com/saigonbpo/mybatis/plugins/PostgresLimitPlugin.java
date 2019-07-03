package com.saigonbpo.mybatis.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;


public class PostgresLimitPlugin extends PluginAdapter {

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();

		Field limit = new Field();
		limit.setName("limit");
		limit.setVisibility(JavaVisibility.PRIVATE);
		limit.setType(integerWrapper);
		topLevelClass.addField(limit);

		Method setLimit = new Method();
		setLimit.setVisibility(JavaVisibility.PUBLIC);
		setLimit.setName("setLimit");
		setLimit.addParameter(new Parameter(integerWrapper, "limit"));
		setLimit.addBodyLine("this.limit = limit;");
		topLevelClass.addMethod(setLimit);

		Method getLimit = new Method();
		getLimit.setVisibility(JavaVisibility.PUBLIC);
		getLimit.setReturnType(integerWrapper);
		getLimit.setName("getLimit");
		getLimit.addBodyLine("return limit;");
		topLevelClass.addMethod(getLimit);

		Field offset = new Field();
		offset.setName("offset");
		offset.setVisibility(JavaVisibility.PRIVATE);
		offset.setType(integerWrapper);
		topLevelClass.addField(offset);

		Method setOffset = new Method();
		setOffset.setVisibility(JavaVisibility.PUBLIC);
		setOffset.setName("setOffset");
		setOffset.addParameter(new Parameter(integerWrapper, "offset"));
		setOffset.addBodyLine("this.offset = offset;");
		topLevelClass.addMethod(setOffset);

		Method getOffset = new Method();
		getOffset.setVisibility(JavaVisibility.PUBLIC);
		getOffset.setReturnType(integerWrapper);
		getOffset.setName("getOffset");
		getOffset.addBodyLine("return offset;");
		topLevelClass.addMethod(getOffset);
		
		Method method = new Method(); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("createCriteria");
        method.addParameter(new Parameter(integerWrapper, "limit"));
        method.addParameter(new Parameter(integerWrapper, "offset"));
        method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());
        method.addBodyLine("Criteria criteria = createCriteriaInternal();"); //$NON-NLS-1$
        method.addBodyLine("if (oredCriteria.size() == 0) {"); //$NON-NLS-1$
        method.addBodyLine("oredCriteria.add(criteria);"); //$NON-NLS-1$
        method.addBodyLine("}"); //$NON-NLS-1$
        method.addBodyLine("this.offset = offset;");
        method.addBodyLine("this.limit = limit;");
        method.addBodyLine("return criteria;"); //$NON-NLS-1$
		topLevelClass.addMethod(method);
		
		return true;
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {

		XmlElement ifLimitNotNullElement = new XmlElement("if");
		ifLimitNotNullElement.addAttribute(new Attribute("test", "limit != null"));

		XmlElement ifOffsetNotNullElement = new XmlElement("if");
		ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
		ifOffsetNotNullElement.addElement(new TextElement("limit ${limit} offset  ${offset}"));
		ifLimitNotNullElement.addElement(ifOffsetNotNullElement);

		XmlElement ifOffsetNullElement = new XmlElement("if");
		ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
		ifOffsetNullElement.addElement(new TextElement("limit ${limit}"));
		ifLimitNotNullElement.addElement(ifOffsetNullElement);

		element.addElement(ifLimitNotNullElement);

		return true;
	}

	public boolean validate(List<String> arg0) {
		// TODO Auto-generated method stub
		return true;
	}
}
