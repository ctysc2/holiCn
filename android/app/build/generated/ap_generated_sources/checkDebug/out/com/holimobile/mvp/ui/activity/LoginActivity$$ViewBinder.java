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

public class LoginActivity$$ViewBinder<T extends LoginActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131230950, "field 'mUserName'");
    target.mUserName = finder.castView(view, 2131230950, "field 'mUserName'");
    view = finder.findRequiredView(source, 2131230948, "field 'mPwd'");
    target.mPwd = finder.castView(view, 2131230948, "field 'mPwd'");
    view = finder.findRequiredView(source, 2131231005, "field 'mDelete' and method 'onClick'");
    target.mDelete = finder.castView(view, 2131231005, "field 'mDelete'");
    unbinder.view2131231005 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231542, "field 'mPrivacy' and method 'onClick'");
    target.mPrivacy = finder.castView(view, 2131231542, "field 'mPrivacy'");
    unbinder.view2131231542 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231045, "field 'mEye' and method 'onClick'");
    target.mEye = finder.castView(view, 2131231045, "field 'mEye'");
    unbinder.view2131231045 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231038, "field 'mCheck'");
    target.mCheck = finder.castView(view, 2131231038, "field 'mCheck'");
    view = finder.findRequiredView(source, 2131231051, "field 'mLogo'");
    target.mLogo = finder.castView(view, 2131231051, "field 'mLogo'");
    view = finder.findRequiredView(source, 2131231525, "method 'onClick'");
    unbinder.view2131231525 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231098, "method 'onClick'");
    unbinder.view2131231098 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231509, "method 'onClick'");
    unbinder.view2131231509 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231547, "method 'onClick'");
    unbinder.view2131231547 = view;
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

  protected static class InnerUnbinder<T extends LoginActivity> implements Unbinder {
    private T target;

    View view2131231005;

    View view2131231542;

    View view2131231045;

    View view2131231525;

    View view2131231098;

    View view2131231509;

    View view2131231547;

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
      target.mUserName = null;
      target.mPwd = null;
      view2131231005.setOnClickListener(null);
      target.mDelete = null;
      view2131231542.setOnClickListener(null);
      target.mPrivacy = null;
      view2131231045.setOnClickListener(null);
      target.mEye = null;
      target.mCheck = null;
      target.mLogo = null;
      view2131231525.setOnClickListener(null);
      view2131231098.setOnClickListener(null);
      view2131231509.setOnClickListener(null);
      view2131231547.setOnClickListener(null);
    }
  }
}
