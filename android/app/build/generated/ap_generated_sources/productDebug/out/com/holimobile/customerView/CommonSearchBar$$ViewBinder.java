// Generated code from Butter Knife. Do not modify!
package com.holimobile.customerView;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class CommonSearchBar$$ViewBinder<T extends CommonSearchBar> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131230949, "field 'mSearch'");
    target.mSearch = finder.castView(view, 2131230949, "field 'mSearch'");
    view = finder.findRequiredView(source, 2131231042, "field 'mDelete' and method 'onClick'");
    target.mDelete = finder.castView(view, 2131231042, "field 'mDelete'");
    unbinder.view2131231042 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231471, "field 'mCancel' and method 'onClick'");
    target.mCancel = finder.castView(view, 2131231471, "field 'mCancel'");
    unbinder.view2131231471 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends CommonSearchBar> implements Unbinder {
    private T target;

    View view2131231042;

    View view2131231471;

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
      target.mSearch = null;
      view2131231042.setOnClickListener(null);
      target.mDelete = null;
      view2131231471.setOnClickListener(null);
      target.mCancel = null;
    }
  }
}
