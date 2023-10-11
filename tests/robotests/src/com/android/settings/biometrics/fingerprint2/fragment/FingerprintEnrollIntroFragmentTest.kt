/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.biometrics.fingerprint2.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.runner.AndroidJUnit4
import com.android.settings.R
import com.android.settings.biometrics.fingerprint2.ui.enrollment.fragment.FingerprintEnrollIntroV2Fragment
import com.android.settings.biometrics.fingerprint2.ui.enrollment.viewmodel.FingerprintEnrollNavigationViewModel
import com.android.settings.biometrics.fingerprint2.ui.enrollment.viewmodel.FingerprintEnrollViewModel
import com.android.settings.biometrics.fingerprint2.ui.enrollment.viewmodel.FingerprintGatekeeperViewModel
import com.android.settings.biometrics.fingerprint2.ui.enrollment.viewmodel.FingerprintScrollViewModel
import com.android.settings.biometrics.fingerprint2.ui.enrollment.viewmodel.GatekeeperInfo
import com.android.settings.testutils2.FakeFingerprintManagerInteractor
import com.google.android.setupdesign.GlifLayout
import com.google.android.setupdesign.template.RequireScrollMixin
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FingerprintEnrollIntroFragmentTest {
  private var context: Context = ApplicationProvider.getApplicationContext()
  private var interactor = FakeFingerprintManagerInteractor()

  private val gatekeeperViewModel =
    FingerprintGatekeeperViewModel(
      GatekeeperInfo.GatekeeperPasswordInfo(byteArrayOf(1, 2, 3), 100L),
      interactor
    )
  private val backgroundDispatcher = StandardTestDispatcher()
  private lateinit var fragmentScenario: FragmentScenario<FingerprintEnrollIntroV2Fragment>

  private val navigationViewModel =
    FingerprintEnrollNavigationViewModel(
      backgroundDispatcher,
      interactor,
      gatekeeperViewModel,
      canSkipConfirm = true,
    )
  private var fingerprintViewModel = FingerprintEnrollViewModel(interactor, backgroundDispatcher)
  private var fingerprintScrollViewModel = FingerprintScrollViewModel()

  @Before
  fun setup() {
    val factory =
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
          modelClass: Class<T>,
        ): T {
          return when (modelClass) {
            FingerprintEnrollViewModel::class.java -> fingerprintViewModel
            FingerprintScrollViewModel::class.java -> fingerprintScrollViewModel
            FingerprintEnrollNavigationViewModel::class.java -> navigationViewModel
            FingerprintGatekeeperViewModel::class.java -> gatekeeperViewModel
            else -> null
          }
            as T
        }
      }

    fragmentScenario =
      launchFragmentInContainer(Bundle(), R.style.SudThemeGlif) {
        FingerprintEnrollIntroV2Fragment(factory)
      }
  }

  @Test
  fun testScrollToBottomButtonChangesText() {
    fragmentScenario.onFragment { fragment ->
      onView(withText("I agree")).check(doesNotExist())
      val someView = (fragment.requireView().findViewById<GlifLayout>(R.id.setup_wizard_layout))!!
      val scrollMixin = someView.getMixin(RequireScrollMixin::class.java)!!
      val listener = scrollMixin.onRequireScrollStateChangedListener
      // This actually changes the button text
      listener.onRequireScrollStateChanged(false)

      onView(withText("I agree")).check(matches(isDisplayed()))
    }
  }

  @Test
  fun testBasicTitle() {
    onView(withText(R.string.security_settings_fingerprint_enroll_introduction_title))
      .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
  }

  @Test
  fun testFooterMessageTwo() {
    onView(withId(R.id.footer_message_2))
      .check(
        matches(
          withText(
            context.getString(
              (R.string.security_settings_fingerprint_v2_enroll_introduction_footer_message_2)
            )
          )
        )
      )
  }

  @Test
  fun testFooterMessageThree() {
    onView(withId(R.id.footer_message_3))
      .check(
        matches(
          withText(
            context.getString(
              (R.string.security_settings_fingerprint_v2_enroll_introduction_footer_message_3)
            )
          )
        )
      )
  }

  @Test
  fun testFooterMessageFour() {
    onView(withId(R.id.footer_message_4))
      .check(
        matches(
          withText(
            context.getString(
              (R.string.security_settings_fingerprint_v2_enroll_introduction_footer_message_4)
            )
          )
        )
      )
  }

  @Test
  fun testFooterMessageFive() {
    onView(withId(R.id.footer_message_5))
      .check(
        matches(
          withText(
            context.getString(
              (R.string.security_settings_fingerprint_v2_enroll_introduction_footer_message_5)
            )
          )
        )
      )
  }
}
