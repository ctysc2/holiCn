// Generated code from Butter Knife. Do not modify!
package com.holimobile.mvp.ui.activity.common;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ImageGalleryActivity$$ViewBinder<T extends ImageGalleryActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131231442, "field 'mToolBar'");
    target.mToolBar = finder.castView(view, 2131231442, "field 'mToolBar'");
    view = finder.findRequiredView(source, 2131230817, "field 'mBack' and method 'onClick'");
    target.mBack = finder.castView(view, 2131230817, "field 'mBack'");
    unbinder.view2131230817 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = finder.findRequiredView(source, 2131231185, "field 'mMidText'");
    target.mMidText = finder.castView(view, 2131231185, "field 'mMidText'");
    view = finder.findRequiredView(source, 2131231609, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131231609, "field 'mViewPager'");
    view = finder.findRequiredView(source, 2131231598, "field 'mTopSeparate'");
    target.mTopSeparate = view;
    view = finder.findRequiredView(source, 2131231113, "method 'onClick'");
    unbinder.view2131231113 = view;
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

  protected static class InnerUnbinder<T extends ImageGalleryActivity> implements Unbinder {
    private T target;

    View view2131230817;

    View view2131231113;

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
      target.mToolBar = null;
      view2131230817.setOnClickListener(null);
      target.mBack = null;
      target.mMidText = null;
      target.mViewPager = null;
      target.mTopSeparate = null;
      view2131231113.setOnClickListener(null);
    }
  }
}
