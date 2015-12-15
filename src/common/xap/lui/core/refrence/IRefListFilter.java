package xap.lui.core.refrence;



public interface IRefListFilter {
	
	AbstractRefListItem[] filter(String inputValue, IRefModel model);
	
}
