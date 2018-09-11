package android.support.v4.internal.view;

import android.support.annotation.RestrictTo;
import android.view.Menu;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public interface SupportMenu extends Menu {
   int CATEGORY_MASK = -65536;
   int CATEGORY_SHIFT = 16;
   int FLAG_KEEP_OPEN_ON_SUBMENU_OPENED = 4;
   int USER_MASK = 65535;
   int USER_SHIFT = 0;
}
