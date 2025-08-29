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

public class VerifyCodeActivity$$ViewBinder<T extends VerifyCodeActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131230943, "field 'mEt1'");
    target.mEt1 = finder.castView(view, 2131230943, "field 'mEt1'");
    view = finder.findRequiredView(source, 2131230944, "field 'mEt2'");
    target.mEt2 = finder.castView(view, 2131230944, "field 'mEt2'");
    view = finder.findRequiredView(source, 2131230945, "field 'mEt3'");
    target.mEt3 = finder.castView(view, 2131230945, "field 'mEt3'");
    view = finder.findRequiredView(source, 2131230946, "field 'mEt4'");
    target.mEt4 = finder.castView(view, 2131230946, "field 'mEt4'");
    view = finder.findRequiredView(source, 2131231554, "field 'mSendTo'");
    target.mSendTo = finder.castView(view, 2131231554, "field 'mSendTo'");
    view = finder.findRequiredView(source, 2131231544, "field 'mResetSms' and method 'onClick'");
    target.mResetSms = finder.castView(view, 2131231544, "field 'mResetSms'");
    unbinder.view2131231544 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230817, "method 'onClick'");
    unbinder.view2131230817 = view;
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

  protected static class InnerUnbinder<T extends VerifyCodeActivity> implements Unbinder {
    private T target;

    View view2131231544;

    View view2131230817;

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
      target.mEt1 = null;
      target.mEt2 = null;
      target.mEt3 = null;
      target.mEt4 = null;
      target.mSendTo = null;
      view2131231544.setOnClickListener(null);
      target.mResetSms = null;
      view2131230817.setOnClickListener(null);
    }
  }
}
