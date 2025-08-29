// Generated code from Butter Knife. Do not modify!
package com.holimobile.mvp.ui.activity;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class SettingActivity$$ViewBinder<T extends SettingActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231185, "field 'midText'");
    target.midText = finder.castView(view, 2131231185, "field 'midText'");
    view = finder.findRequiredView(source, 2131231470, "field 'mCache'");
    target.mCache = finder.castView(view, 2131231470, "field 'mCache'");
    view = finder.findRequiredView(source, 2131231574, "field 'mVersion'");
    target.mVersion = finder.castView(view, 2131231574, "field 'mVersion'");
    view = finder.findRequiredView(source, 2131230817, "method 'onClick'");
    unbinder.view2131230817 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231101, "method 'onClick'");
    unbinder.view2131231101 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231131, "method 'onClick'");
    unbinder.view2131231131 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231526, "method 'onClick'");
    unbinder.view2131231526 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231093, "method 'onClick'");
    unbinder.view2131231093 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231141, "method 'onClick'");
    unbinder.view2131231141 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231147, "method 'onClick'");
    unbinder.view2131231147 = view;
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

  protected static class InnerUnbinder<T extends SettingActivity> implements Unbinder {
    private T target;

    View view2131230817;

    View view2131231101;

    View view2131231131;

    View view2131231526;

    View view2131231093;

    View view2131231141;

    View view2131231147;

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
      target.midText = null;
      target.mCache = null;
      target.mVersion = null;
      view2131230817.setOnClickListener(null);
      view2131231101.setOnClickListener(null);
      view2131231131.setOnClickListener(null);
      view2131231526.setOnClickListener(null);
      view2131231093.setOnClickListener(null);
      view2131231141.setOnClickListener(null);
      view2131231147.setOnClickListener(null);
    }
  }
}
