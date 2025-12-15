# Product Requirements Document: In-App Purchase - Remove Ads

## Introduction/Overview

This feature implements a subscription-based in-app purchase that allows users to remove all advertisements from the Screen Timer app. Users can subscribe on a monthly or yearly basis to enjoy an ad-free experience. The purchase is implemented using Google Play Billing Library (native Android solution) to avoid third-party dependencies and user account management complexity.

**Problem Statement**: Users who frequently use the app may find ads disruptive to their experience. Providing a simple way to remove ads creates a premium experience while generating recurring revenue.

**Goal**: Implement a seamless, subscription-based in-app purchase system that removes all ads (banner, interstitial, and app open ads) when users subscribe, with automatic purchase restoration across devices.

## Goals

1. Enable users to subscribe to remove all ads from the app
2. Provide both monthly and yearly subscription options
3. Implement automatic purchase verification on app launch
4. Ensure purchases are automatically restored when users reinstall the app or switch devices
5. Display purchase status clearly in the UI
6. Create a smooth, conversion-optimized purchase flow
7. Comply with Google Play billing policies and guidelines

## User Stories

### As a frequent user
- I want to remove all ads from the app so that I can use the timer without interruptions
- I want to choose between monthly and yearly payment options so I can pick what fits my budget
- I want the purchase to work immediately after payment so I see results right away
- I want my purchase to be automatically restored if I reinstall the app so I don't have to pay again

### As a new user
- I want to clearly see what benefits the subscription provides before purchasing
- I want to trust that the payment process is secure through Google Play
- I want to see pricing upfront with no hidden fees

### As a returning user (after reinstall)
- I want my previous purchase to be automatically detected so I don't see ads again
- I want a manual "Restore Purchase" option in case automatic restore fails

## Functional Requirements

### FR-1: Google Play Billing Integration

1.1. App must integrate Google Play Billing Library version 6.x or later

1.2. App must support in-app subscriptions (not one-time purchases)

1.3. App must define two subscription products:
- Monthly subscription (e.g., "remove_ads_monthly")
- Yearly subscription (e.g., "remove_ads_yearly" with discount vs monthly)

1.4. App must handle all billing states:
- Purchased (active subscription)
- Pending (payment processing)
- Unspecified (no purchase or expired)
- Cancelled (user cancelled but may still be in grace period)

1.5. App must verify purchase signatures to prevent fraud

### FR-2: Purchase UI on Main Screen

2.1. Main screen must display a prominent "Remove Ads" button/card when user does NOT have an active subscription

2.2. Button/card must be visually distinct but not intrusive to core timer functionality

2.3. Tapping the button must open a purchase dialog/bottom sheet showing:
- Feature benefits ("Enjoy ad-free experience")
- Monthly subscription price
- Yearly subscription price with savings percentage
- "Subscribe Monthly" button
- "Subscribe Yearly" button
- Terms of service and privacy policy links
- "Maybe Later" or dismiss option

2.4. Purchase UI must be hidden completely when user has an active subscription

2.5. Purchase UI must show loading state during purchase processing

2.6. Purchase UI must show success confirmation after successful purchase

2.7. Purchase UI must show clear error messages if purchase fails (network error, user cancelled, etc.)

### FR-3: Ad Removal Logic

3.1. App must check subscription status on every app launch (cold start)

3.2. App must NOT display any ads (banner, interstitial, app open) when user has active subscription

3.3. App must resume showing ads immediately if subscription expires or is cancelled

3.4. Ad removal state must persist across app restarts without requiring network calls every time (cache subscription status)

3.5. App must re-verify subscription status at least once per day to detect cancellations/expirations

3.6. All ad-related code must check subscription status before showing ads:
```kotlin
if (!subscriptionManager.hasActiveSubscription()) {
    // Show ad
}
```

### FR-4: Purchase Restoration

4.1. App must automatically check for existing purchases on first launch after install

4.2. App must automatically restore subscription when user signs in with same Google account on new device

4.3. App must provide a "Restore Purchases" button in app settings

4.4. Restore button must:
- Show loading indicator during restoration
- Display success message if purchase found
- Display "No purchases found" message if no active subscription exists
- Handle errors gracefully (network issues, etc.)

4.5. Restoration must complete within 5 seconds or show timeout error

### FR-5: Subscription Management

5.1. App must provide a "Manage Subscription" button in settings when user has active subscription

5.2. "Manage Subscription" button must open Google Play subscription management page

5.3. Settings must display current subscription status:
- "No active subscription" (with option to subscribe)
- "Monthly subscription - Active" (with manage/cancel options)
- "Yearly subscription - Active" (with manage/cancel options)
- "Subscription expired on [date]" (with option to resubscribe)

5.4. App must handle subscription lifecycle events:
- New purchase
- Renewal
- Cancellation (user still has access until period ends)
- Expiration
- Grace period (payment failed but Google allows temporary access)
- Account hold (payment failed, subscription paused)

### FR-6: Testing and Development

6.1. App must support Google Play test purchases for development/testing

6.2. App must use separate product IDs for dev/staging/production builds

6.3. Developer must document how to set up test accounts in Google Play Console

6.4. App must log all billing events for debugging (in debug builds only)

### FR-7: Error Handling

7.1. App must handle all possible billing errors:
- Network unavailable
- Billing unavailable (device doesn't support Google Play)
- Item unavailable (product not configured correctly)
- User cancelled purchase
- Item already owned
- Developer error (setup issue)

7.2. Each error must show user-friendly message explaining what happened and next steps

7.3. App must never crash due to billing errors

7.4. Critical errors must be logged for monitoring (Firebase Crashlytics)

## Non-Goals (Out of Scope)

1. **User account system** - No custom login or account management. Purchase is tied to Google account only.

2. **Multiple subscription tiers** - Only one tier: "Remove Ads". No premium features beyond ad removal.

3. **Free trial period** - Initial implementation will not include free trials. Can be added later.

4. **Promotional codes** - Not implementing promo codes in first version.

5. **Family sharing** - Google Play Family Library sharing is not enabled for this subscription.

6. **Partial ad removal** - Cannot choose which ads to remove. It's all or nothing.

7. **One-time purchase option** - Only subscriptions, not lifetime purchase.

8. **Revenue analytics beyond Google Play** - Will rely on Google Play Console for revenue reporting. No additional analytics platform.

9. **Cross-platform purchases** - Only works on Android via Google Play. No iOS/web support.

## Design Considerations

### Main Screen Integration

- Add a Material 3 Card or prominent button below the timer controls
- Use accent color to draw attention without being annoying
- Card should say something like:
  - Title: "Enjoy Ad-Free Experience"
  - Subtitle: "Remove all ads with a subscription"
  - Button: "View Plans"
- Hide completely when user has active subscription

### Purchase Dialog/Bottom Sheet

- Use Material 3 ModalBottomSheet for purchase flow
- Show subscription benefits at top with icons:
  - ✓ No banner ads
  - ✓ No interstitial ads
  - ✓ No app open ads
  - ✓ Uninterrupted timer experience
- Display pricing cards for monthly and yearly options
- Highlight yearly option with "Save X%" badge
- Use primary button color for "Subscribe" buttons
- Include small links to privacy policy and terms

### Settings Screen

- Add "Subscription" section in settings
- Show current status with appropriate icon
- Include "Restore Purchases" button
- Include "Manage Subscription" button (if active)

### Visual Polish

- Use subtle animations for purchase flow
- Show success animation when purchase completes
- Ensure dark mode support for all new UI

## Technical Considerations

### Dependencies

```kotlin
// Add to app/build.gradle.kts
implementation("com.android.billingclient:billing-ktx:6.1.0")
```

### Architecture

1. **Create `BillingManager` singleton** to handle all billing operations:
   - Initialize billing client
   - Query products
   - Launch purchase flow
   - Verify purchases
   - Restore purchases
   - Check subscription status

2. **Create `SubscriptionRepository`** to manage subscription state:
   - Cache subscription status locally
   - Expose subscription status as Flow/LiveData
   - Refresh status periodically

3. **Update ad-related code**:
   - Modify `AdMobBanner.kt` to check subscription before loading
   - Modify `AppOpenAdManager.kt` to check subscription before showing
   - Add subscription check to any interstitial ad logic

4. **Add new UI components**:
   - `PurchaseDialog.kt` - Bottom sheet for subscription options
   - Update `MainScreen.kt` to show "Remove Ads" card
   - Update settings screen with subscription section

### Product IDs

Configure in `build.gradle.kts`:

```kotlin
buildConfigField("String", "SUBSCRIPTION_MONTHLY_ID", "\"remove_ads_monthly\"")
buildConfigField("String", "SUBSCRIPTION_YEARLY_ID", "\"remove_ads_yearly\"")
```

For dev builds, use test product IDs:
```kotlin
buildConfigField("String", "SUBSCRIPTION_MONTHLY_ID", "\"android.test.purchased\"")
```

### Data Storage

- Store subscription status in DataStore (encrypted)
- Cache expiry time to know when to re-verify
- Store last verification timestamp

### Google Play Console Setup

1. Create subscription products in Play Console
2. Set up pricing for monthly and yearly subscriptions
3. Configure test accounts for testing
4. Add app to closed testing track for testing real purchases

### Security

- Verify purchase tokens on client side
- Consider server-side verification for production (optional but recommended)
- Never trust client-side subscription status without verification

## Success Metrics

1. **Conversion Rate**: Track percentage of users who subscribe after viewing purchase dialog
   - Target: 2-5% conversion rate

2. **Revenue**: Monitor monthly recurring revenue from subscriptions
   - Target: Set based on user base size

3. **Retention**: Measure subscription retention rate after 1, 3, 6 months
   - Target: >70% retention after 1 month

4. **Churn Rate**: Track subscription cancellations
   - Target: <10% monthly churn

5. **Restore Success Rate**: Percentage of restore attempts that succeed
   - Target: >95% success rate

6. **Purchase Flow Completion**: Users who start purchase vs. complete purchase
   - Target: >60% completion rate

7. **Error Rate**: Percentage of purchase attempts that fail due to errors
   - Target: <5% error rate

## Implementation Steps (High-Level)

1. Set up Google Play Console with subscription products
2. Add billing library dependency
3. Create `BillingManager` singleton class
4. Create `SubscriptionRepository` for state management
5. Implement purchase verification logic
6. Create purchase UI (bottom sheet)
7. Add "Remove Ads" card to main screen
8. Update ad loading logic to check subscription status
9. Add subscription settings screen
10. Implement restore purchases functionality
11. Add error handling and logging
12. Test with Google Play test accounts
13. Test subscription lifecycle (purchase, renewal, cancellation)
14. Update ProGuard rules if needed
15. Deploy to internal testing track for real purchase testing

## Open Questions

1. **Pricing**: What should the monthly and yearly subscription prices be?
   - Suggestion: Research competitor apps for pricing benchmarks
   - Consider offering yearly at 10-month price to incentivize

2. **Grace Period**: Should we show grace period status in UI when payment fails?
   - Recommendation: Yes, inform user their payment failed and needs updating

3. **Server-Side Verification**: Do we need backend server to verify purchases?
   - Recommendation: Not required initially, but consider for security at scale

4. **Analytics**: Beyond Play Console, do we need custom analytics for purchase events?
   - Recommendation: Log key events to Firebase Analytics (purchase_started, purchase_completed, etc.)

5. **A/B Testing**: Should we test different pricing or UI placements?
   - Recommendation: Start with one implementation, gather data, then test variations

6. **Refund Policy**: How should app handle refunded subscriptions?
   - Recommendation: Google handles this automatically - restore ads if subscription refunded

## References

- [Google Play Billing Library Documentation](https://developer.android.com/google/play/billing)
- [Subscription Best Practices](https://developer.android.com/google/play/billing/subscriptions)
- [Testing In-App Purchases](https://developer.android.com/google/play/billing/test)

---

**Document Version**: 1.0
**Last Updated**: 2025-11-29
**Status**: Ready for Review
