package xap.lui.core.xml;


public class TTVo implements java.io.Serializable
{
   public int F1;

   public String F2;

   
   public TTVo()
   {
      super();
   }

   
   public int getF1()
   {
      return F1;
   }

   
   public java.lang.String getF2()
   {
      return F2;
   }

   
   public void setF1(int newF1)
   {
      F1 = newF1;
   }

   
   public void setF2(java.lang.String newF2)
   {
      F2 = newF2;
   }

   public String toString()
   {
      return TTVo.class + ":" + F1 + ":" + F2;
   }
}
