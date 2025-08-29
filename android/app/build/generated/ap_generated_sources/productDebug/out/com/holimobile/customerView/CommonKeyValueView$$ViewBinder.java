// Generated code from Butter Knife. Do not modify!
package com.holimobile.customerView;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class CommonKeyValueView$$ViewBinder<T extends CommonKeyValueView> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231519, "field 'mKey'");
    target.mKey = finder.castView(view, 2131231519, "field 'mKey'");
    view = finder.findRequiredView(source, 2131231573, "field 'mValue'");
    target.mValue = finder.castView(view, 2131231573, "field 'mValue'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends CommonKeyValueView> implements Unbinder {
    private T target;

    protected InnerUnbinder(T target) {
      this.target = target;
    }

    @Override
    public final void unbind() {
      if (target == null) throw new IllegalStateException("Bindings already cleared.");
      unbind(target);
      target = null;
    }

    protected void unbind(T target) {
      target.mKey = null;
      target.mValue = null;
    }
  }
}
