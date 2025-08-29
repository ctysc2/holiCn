// Generated code from Butter Knife. Do not modify!
package com.holimobile.mvp.ui.activity.webview;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class WebViewActivity$$ViewBinder<T extends WebViewActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231185, "field 'midText'");
    target.midText = finder.castView(view, 2131231185, "field 'midText'");
    view = finder.findRequiredView(source, 2131231613, "field 'mWebView'");
    target.mWebView = finder.castView(view, 2131231613, "field 'mWebView'");
    view = finder.findRequiredView(source, 2131231261, "field 'mPgBar'");
    target.mPgBar = finder.castView(view, 2131231261, "field 'mPgBar'");
    view = finder.findRequiredView(source, 2131231442, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131231442, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131231314, "field 'mRight'");
    target.mRight = finder.castView(view, 2131231314, "field 'mRight'");
    view = finder.findRequiredView(source, 2131231000, "field 'mRightButton' and method 'onClick'");
    target.mRightButton = finder.castView(view, 2131231000, "field 'mRightButton'");
    unbinder.view2131231000 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131230967, "field 'mLayout'");
    target.mLayout = finder.castView(view, 2131230967, "field 'mLayout'");
    view = finder.findRequiredView(source, 2131231117, "field 'mErrorContainer'");
    target.mErrorContainer = finder.castView(view, 2131231117, "field 'mErrorContainer'");
    view = finder.findRequiredView(source, 2131230817, "method 'onClick'");
    unbinder.view2131230817 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231548, "method 'onClick'");
    unbinder.view2131231548 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231474, "method 'onClick'");
    unbinder.view2131231474 = view;
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

  protected static class InnerUnbinder<T extends WebViewActivity> implements Unbinder {
    private T target;

    View view2131231000;

    View view2131230817;

    View view2131231548;

    View view2131231474;

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
      target.mWebView = null;
      target.mPgBar = null;
      target.mToolBar = null;
      target.mRight = null;
      view2131231000.setOnClickListener(null);
      target.mRightButton = null;
      target.mLayout = null;
      target.mErrorContainer = null;
      view2131230817.setOnClickListener(null);
      view2131231548.setOnClickListener(null);
      view2131231474.setOnClickListener(null);
    }
  }
}
