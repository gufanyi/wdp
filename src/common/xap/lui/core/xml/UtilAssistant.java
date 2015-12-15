package xap.lui.core.xml;

import java.lang.reflect.*;
import java.security.*;

public class UtilAssistant
{
   private Field fa[] = null;

   
   public UtilAssistant()
   {
      super();
   }

   public Field[] getAllFields(final Class cl)
   {
      if (cl == Object.class)
         return null;
      AccessController.doPrivileged(new PrivilegedAction()
      {
         public Object run()
         {
            fa = cl.getDeclaredFields();
            return null;
         }
      });
      Class superClass = cl.getSuperclass();
      Field faThis[] = fa;
      Field faSuper[] = getAllFields(superClass);
      if (faSuper == null || faSuper.length == 0)
         return faThis;
      Field faAll[] = new Field[faThis.length + faSuper.length];
      System.arraycopy(faSuper, 0, faAll, 0, faSuper.length);
      System.arraycopy(faThis, 0, faAll, faSuper.length, faThis.length);
      return faAll;
   }
}
