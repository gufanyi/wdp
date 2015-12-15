package xap.lui.core.decompres;

import java.util.ArrayList;
import java.util.List;



public class DataDefineException extends LuiInfoPathParserException {
	public static final int C_iStorageErrode_NotConnectDB = 1001;
	public static final int C_iStorageErrode_CreateTable = 1002;
	public static final int C_iStorageErrode_ModifyTable = 1003;
	public static final int C_iStorageErrode_NottypeMatch = 1004;
	public static final int C_iStorageErrode_NotsendNode = 1005;
	public static final int C_iStorageErrode_NotsendValue = 1006;
	public static final int C_iDbOperErrode_NoData = 1007;
	public static final int C_iDbOperErrode_SaveError = 1008;
	public static final int C_iDbOperErrode_DelError = 1009;
	public static final int C_iDbOperErrode_UpdateError = 1010;
	public static final int C_iDbOperErrode_QueryError = 1011;
	public static final int C_iStorageErrode_AttributenotDefine = 1012;
	public static final int C_iStorageErrode_AttributeDefineError = 1013;
	public static final int C_iStorageErrode_CreateKindDefeated = 1014;
	public static final int C_iStorageErrode_CreateKDNotImpInter = 1015;
	public static final int C_iStorageErrode_NodeError = 1016;
	public static final int C_iStorageErrode_NodeAbsenceAttribute = 1017;
	public static final int C_iDbOperErrode_ReadError = 1018;
	public static final int C_iStorageErrode_DocumentError = 1019;
	public static final int C_iStorageErrode_ParseError = 1020;
	public static final int C_iStorageErrode_InstanceError = 1021;
	public static final int C_iStorageErrode_WriteAbnormity = 1022;
	public static final int C_iStorageErrode_MethodnotAdmin = 1023;
	public static final int C_iDbOperErrode_StytemNotType = 1024;
	public static final int C_iDbOperErrode_CharacterEmpty = 1025;
	public static final int C_iDbOperErrode_CharacterNotFormat = 1026;
	public static final int C_iDbOperErrode_CharacterIDNotNum = 1027;
	public static final int C_iDbOperErrode_SendNodeNotFormat = 1028;
	public static final int C_iDbOperErrode_NotInitialize = 1029;
	public static final int C_iDbOperErrode_NotDesignDefine = 1030;
	public static final int C_iStorageErrode_SonNodeDefineError = 1031;
	public static final int C_iDbOperErrode_BringTypeError = 1032;
	public static final int C_iDbOperErrode_SendNotType = 1033;
	public static final int C_iStorageErrode_FileNotExist = 1034;
	public static final int C_iStorageErrode_NameNotFind = 1035;
	public static final int C_iStorageErrode_RootNodeNotFind = 1036;
	public static final int C_iStorageErrode_NotFindDataDefine = 1037;
	public static final int C_iDbOperErrode_PrimaryIsNull = 1038;
	public static final int C_iDbOperErrode_NotBindFile = 1039;
	public static final int C_iDbOperErrode_NotDataBind = 1040;
	public static final int C_iDbOperErrode_MasterAdd = 1041;
	public static final int C_iDbOperErrode_DelNotDefine = 1042;
	public static final int C_iDbOperErrode_DelNotFind = 1043;
	public static final int C_iDbOperErrode_FieldTooLong = 1044;
	private static final prvAdapter fAdapter = new prvAdapter();
	private static final long serialVersionUID = 7396949674282833766L;
	private List<LuiInfoPathParserException> list = new ArrayList();

	public DataDefineException(int aErrCode) {
		super(aErrCode);
		setAutoUserMsg(fAdapter);
	}

	public DataDefineException(int aErrCode, String message, Throwable cause) {
		super(aErrCode, message, cause);
		setAutoUserMsg(fAdapter);
	}

	public DataDefineException(int aErrCode, String message) {
		super(aErrCode, message);
		setAutoUserMsg(fAdapter);
	}

	public DataDefineException(int aErrCode, String message,
			String toUserMessage) {
		super(aErrCode, message);
		setToUserMsg(toUserMessage);
	}

	public DataDefineException(int aErrCode, Throwable cause) {
		super(aErrCode, cause);
		setAutoUserMsg(fAdapter);
	}

	public List<LuiInfoPathParserException> getList() {
		return this.list;
	}

	public void setList(List<LuiInfoPathParserException> list) {
		this.list = list;
	}

	private static class prvAdapter implements
			LuiInfoPathParserException.IAutoUserMsgAdapter {
		public String getUserMsg(int aErrCode) {
			switch (aErrCode) {
			// case 1001:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotConnectDB", new
			// Object[0]);
			// case 1002:
			// return
			// Constantform.getString4CurrentUser("DataDefine.CreateTable", new
			// Object[0]);
			// case 1003:
			// return
			// Constantform.getString4CurrentUser("DataDefine.ModifyTable", new
			// Object[0]);
			// case 1004:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NottypeMatch", new
			// Object[0]);
			// case 1005:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotsendNode", new
			// Object[0]);
			// case 1006:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotsendValue", new
			// Object[0]);
			// case 1007:
			// return Constantform.getString4CurrentUser("DataDefine.NoData",
			// new Object[0]);
			// case 1008:
			// return Constantform.getString4CurrentUser("DataDefine.SaveError",
			// new Object[0]);
			// case 1009:
			// return Constantform.getString4CurrentUser("DataDefine.DelError",
			// new Object[0]);
			// case 1010:
			// return
			// Constantform.getString4CurrentUser("DataDefine.UpdateError", new
			// Object[0]);
			// case 1011:
			// return
			// Constantform.getString4CurrentUser("DataDefine.QueryError", new
			// Object[0]);
			// case 1012:
			// return
			// Constantform.getString4CurrentUser("DataDefine.AttributenotDefine",
			// new Object[0]);
			// case 1013:
			// return
			// Constantform.getString4CurrentUser("DataDefine.AttributeDefineError",
			// new Object[0]);
			// case 1014:
			// return
			// Constantform.getString4CurrentUser("DataDefine.CreateKindDefeated",
			// new Object[0]);
			// case 1015:
			// return
			// Constantform.getString4CurrentUser("DataDefine.CreateKDNotImpInter",
			// new Object[0]);
			// case 1016:
			// return Constantform.getString4CurrentUser("DataDefine.NodeError",
			// new Object[0]);
			// case 1017:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NodeAbsenceAttribute",
			// new Object[0]);
			// case 1018:
			// return Constantform.getString4CurrentUser("DataDefine.ReadError",
			// new Object[0]);
			// case 1019:
			// return
			// Constantform.getString4CurrentUser("DataDefine.DocumentError",
			// new Object[0]);
			// case 1020:
			// return
			// Constantform.getString4CurrentUser("DataDefine.ParseError", new
			// Object[0]);
			// case 1021:
			// return
			// Constantform.getString4CurrentUser("DataDefine.InstanceError",
			// new Object[0]);
			// case 1022:
			// return
			// Constantform.getString4CurrentUser("DataDefine.WriteAbnormity",
			// new Object[0]);
			// case 1023:
			// return
			// Constantform.getString4CurrentUser("DataDefine.MethodnotAdmin",
			// new Object[0]);
			// case 1024:
			// return
			// Constantform.getString4CurrentUser("DataDefine.StytemNotType",
			// new Object[0]);
			// case 1025:
			// return
			// Constantform.getString4CurrentUser("DataDefine.CharacterEmpty",
			// new Object[0]);
			// case 1026:
			// return
			// Constantform.getString4CurrentUser("DataDefine.CharacterNotFormat",
			// new Object[0]);
			// case 1027:
			// return
			// Constantform.getString4CurrentUser("DataDefine.CharacterIDNotNum",
			// new Object[0]);
			// case 1028:
			// return
			// Constantform.getString4CurrentUser("DataDefine.SendNodeNotFormat",
			// new Object[0]);
			// case 1029:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotInitialize",
			// new Object[0]);
			// case 1030:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotDesignDefine",
			// new Object[0]);
			// case 1031:
			// return
			// Constantform.getString4CurrentUser("DataDefine.SonNodeDefineError",
			// new Object[0]);
			// case 1032:
			// return
			// Constantform.getString4CurrentUser("DataDefine.BringTypeError",
			// new Object[0]);
			// case 1033:
			// return
			// Constantform.getString4CurrentUser("DataDefine.SendNotType", new
			// Object[0]);
			// case 1034:
			// return
			// Constantform.getString4CurrentUser("DataDefine.FileNotExist", new
			// Object[0]);
			// case 1035:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NameNotFind", new
			// Object[0]);
			// case 1036:
			// return
			// Constantform.getString4CurrentUser("DataDefine.RootNodeNotFind",
			// new Object[0]);
			// case 1037:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotFindDataDefine",
			// new Object[0]);
			// case 1038:
			// return
			// Constantform.getString4CurrentUser("DataDefine.PrimaryIsNull",
			// new Object[0]);
			// case 1039:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotBindFile", new
			// Object[0]);
			// case 1040:
			// return
			// Constantform.getString4CurrentUser("DataDefine.NotDataBind", new
			// Object[0]);
			// case 1041:
			// return Constantform.getString4CurrentUser("DataDefine.MasterAdd",
			// new Object[0]);
			// case 1042:
			// return
			// Constantform.getString4CurrentUser("DataDefine.DelNotDefine", new
			// Object[0]);
			// case 1043:
			// return
			// Constantform.getString4CurrentUser("DataDefine.DelNotFind", new
			// Object[0]);

			case 1001:
				return "DataDefine.NotConnectDB";
			case 1002:
				return "DataDefine.CreateTable";
			case 1003:
				return "DataDefine.ModifyTable";
			case 1004:
				return "DataDefine.NottypeMatch";
			case 1005:
				return "DataDefine.NotsendNode";
			case 1006:
				return "DataDefine.NotsendValue";
			case 1007:
				return "DataDefine.NoData";
			case 1008:
				return "DataDefine.SaveError";
			case 1009:
				return "DataDefine.DelError";
			case 1010:
				return "DataDefine.UpdateError";
			case 1011:
				return "DataDefine.QueryError";
			case 1012:
				return "DataDefine.AttributenotDefine";
			case 1013:
				return "DataDefine.AttributeDefineError";
			case 1014:
				return "DataDefine.CreateKindDefeated";
			case 1015:
				return "DataDefine.CreateKDNotImpInter";
			case 1016:
				return "DataDefine.NodeError";
			case 1017:
				return "DataDefine.NodeAbsenceAttribute";
			case 1018:
				return "DataDefine.ReadError";
			case 1019:
				return "DataDefine.DocumentError";
			case 1020:
				return "DataDefine.ParseError";
			case 1021:
				return "DataDefine.InstanceError";
			case 1022:
				return "DataDefine.WriteAbnormity";
			case 1023:
				return "DataDefine.MethodnotAdmin";
			case 1024:
				return "DataDefine.StytemNotType";
			case 1025:
				return "DataDefine.CharacterEmpty";
			case 1026:
				return "DataDefine.CharacterNotFormat";
			case 1027:
				return "DataDefine.CharacterIDNotNum";
			case 1028:
				return "DataDefine.SendNodeNotFormat";
			case 1029:
				return "DataDefine.NotInitialize";
			case 1030:
				return "DataDefine.NotDesignDefine";
			case 1031:
				return "DataDefine.SonNodeDefineError";
			case 1032:
				return "DataDefine.BringTypeError";
			case 1033:
				return "DataDefine.SendNotType";
			case 1034:
				return "DataDefine.FileNotExist";
			case 1035:
				return "DataDefine.NameNotFind";
			case 1036:
				return "DataDefine.RootNodeNotFind";
			case 1037:
				return "DataDefine.NotFindDataDefine";
			case 1038:
				return "DataDefine.PrimaryIsNull";
			case 1039:
				return "DataDefine.NotBindFile";
			case 1040:
				return "DataDefine.NotDataBind";
			case 1041:
				return "DataDefine.MasterAdd";
			case 1042:
				return "DataDefine.DelNotDefine";
			case 1043:
				return "DataDefine.DelNotFind";
			}
			return "";
		}
	}
}
