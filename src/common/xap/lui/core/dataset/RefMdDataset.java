package xap.lui.core.dataset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dataset.IRefDataset;
@XmlRootElement(name="RefMdDataset")
@XmlAccessorType(XmlAccessType.NONE)
public class RefMdDataset extends MdDataset implements IRefDataset {
	private static final long serialVersionUID = 1535975016041386754L;
}
