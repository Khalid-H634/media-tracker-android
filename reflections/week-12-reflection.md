# Week 12 Reflection

**Name:** Khalid Hassan
**Date:** 08-06-2026

---
**My assigned bonus feature:** *(Write Review / Quotes / Priorities)* 

Review
        
## Commits This Week


**Link:**  https://github.com/Khalid-H634/media-tracker-android/pull/11


---

## Code Review



**Reviewed:** *(pod mate's name)*
**Link to my review:**
        Hunter Bammert-Mueller:
https://github.com/Hunterbounty11/media-tracker-android/pull/11/changes/7edca19aa5151c1f0e1145d6d36ed27fbfb15bf8#r3733547304


### What I Looked At
I reviewed Hunter's Priorities feature implementation, specifically PriorityScreen.kt, 
PriorityViewModel.kt, and the integration with LibraryScreen.kt. I focused on the drag-and-drop 
reordering, error handling, and priority limit logic.

### What I Noticed
The drag and drop uses graphicsLayer for smooth animation without recomposition. The MAX_PRIORITIES
= 5 limit is enforced on both UI and ViewModel layers. Error messages are user friendly and stale 
states are properly cleared.


### Comments I Left
PriorityScreen.kt (Lines 46-49): "Good use of LaunchedEffect with errorMessage as the key. The 
Snackbar only shows when the error changes, and onDismissError() prevents duplicate messages."

PriorityViewModel.kt (Lines 36-44): "The error message is user-friendly and actionable. Clearing 
the error state before loading prevents stale messages."


## Bonus Feature — Final Status

<!-- Be concrete and honest. This is your last chance to flag something before demos.
     What does your feature actually do, end to end, right now? What's polished vs. rough?
     Is there anything you know is broken or half-done that you want on my radar before Week 14? -->

**What works end-to-end, right now:**

-Write, edit, and delete reviews (POST, PUT, DELETE)
-Own review shown first with Edit/Delete buttons
-"Write a Review" hidden when own review exists
-Loading, error, and empty states
-409 conflict handled
-Character counter (500 char max)

**Tests written for this feature:**
StarRatingRowTest.kt for the Compose UI test

**Known gaps or rough edges going into demos:**

Edit screen relies on navigation arguments since there's no GET /reviews/{id} endpoint.


## One Thing I Understood More Deeply

<!-- Looking back across both bonus feature weeks — not just this week — what's the one thing that
     actually shifted in how you think about building a feature from scratch, start to finish? -->

Building the feature from scratch in the last two weeks taught me to think about it end to end.
I started with just the form and POST, then added edit, delete, and all the edge cases: network 
failures, duplicate submissions, and navigation issues not just making the happy path work. 
The optimistic delete pattern (removing immediately/reverting on failure) made the app feel faster.

## One Thing I'm Still Confused About
This week I ran into a navigation crash: "navigation destination login is not a direct child of this
NavGraph." I fixed it by updating startDestination from Routes.LOGIN to
"${Routes.LOGIN}?registered=false" so the route matched the composable pattern. I understand the 
fix, but I'm still confused about how optional navigation parameters work with startDestination 
and want to understand this better.


## Anything Else *(optional)*

<!-- Anything about the bonus feature sprint as a whole — the two-week format, being assigned a
     feature rather than choosing it, whatever's on your mind — is fair game here. -->



## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Honest final-status report — what works end-to-end, what's rough, what's tested — plus a specific, genuine "Understood More Deeply" that reflects on the sprint as a whole, not just this week. | Present but vague, or only reports on this week rather than the feature's overall state. | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** same as every other week — I check the link before grading.
