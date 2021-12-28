package model.transfer.importer;

import org.eclipse.uml2.uml.AggregationKind;

/**
 * Asszociáció adatainak reprezentálására létrehozott modell objektum.
 * 
 * @author Horváth Tibor
 *
 */
public class AssociationModel {

	private String modelId;

	private String end1ClassifierName;
	private String end1PropertyName;
	private boolean end1IsNavigable;
	private AggregationKind end1Aggregation;
	private int end1Lower;
	private int end1Upper;

	private String end2ClassifierName;
	private String end2PropertyName;
	private boolean end2IsNavigable;
	private AggregationKind end2Aggregation;
	private int end2Lower;
	private int end2Upper;

	private String comment;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getEnd1ClassifierName() {
		return end1ClassifierName;
	}

	public void setEnd1ClassifierName(String end1InterfaceName) {
		this.end1ClassifierName = end1InterfaceName;
	}

	public String getEnd1PropertyName() {
		return end1PropertyName;
	}

	public void setEnd1PropertyName(String end1PropertyName) {
		this.end1PropertyName = end1PropertyName;
	}

	public boolean isEnd1IsNavigable() {
		return end1IsNavigable;
	}

	public void setEnd1IsNavigable(boolean end1IsNavigable) {
		this.end1IsNavigable = end1IsNavigable;
	}

	public AggregationKind getEnd1Aggregation() {
		return end1Aggregation;
	}

	public void setEnd1Aggregation(AggregationKind end1Aggregation) {
		this.end1Aggregation = end1Aggregation;
	}

	public int getEnd1Lower() {
		return end1Lower;
	}

	public void setEnd1Lower(int end1Lower) {
		this.end1Lower = end1Lower;
	}

	public int getEnd1Upper() {
		return end1Upper;
	}

	public void setEnd1Upper(int end1Upper) {
		this.end1Upper = end1Upper;
	}

	public String getEnd2ClassifierName() {
		return end2ClassifierName;
	}

	public void setEnd2ClassifierName(String end2InterfaceName) {
		this.end2ClassifierName = end2InterfaceName;
	}

	public String getEnd2PropertyName() {
		return end2PropertyName;
	}

	public void setEnd2PropertyName(String end2PropertyName) {
		this.end2PropertyName = end2PropertyName;
	}

	public boolean isEnd2IsNavigable() {
		return end2IsNavigable;
	}

	public void setEnd2IsNavigable(boolean end2IsNavigable) {
		this.end2IsNavigable = end2IsNavigable;
	}

	public AggregationKind getEnd2Aggregation() {
		return end2Aggregation;
	}

	public void setEnd2Aggregation(AggregationKind end2Aggregation) {
		this.end2Aggregation = end2Aggregation;
	}

	public int getEnd2Lower() {
		return end2Lower;
	}

	public void setEnd2Lower(int end2Lower) {
		this.end2Lower = end2Lower;
	}

	public int getEnd2Upper() {
		return end2Upper;
	}

	public void setEnd2Upper(int end2Upper) {
		this.end2Upper = end2Upper;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
