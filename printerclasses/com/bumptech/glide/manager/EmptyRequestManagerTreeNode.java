package com.bumptech.glide.manager;

import com.bumptech.glide.manager.RequestManagerTreeNode;
import java.util.Collections;
import java.util.Set;

final class EmptyRequestManagerTreeNode implements RequestManagerTreeNode {
   public Set getDescendants() {
      return Collections.emptySet();
   }
}
