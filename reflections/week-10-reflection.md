# Week 10 Reflection

**Name:** Khalid Hassan
**Date:** 07-23-2026

---

## Commits This Week



**Link:** https://github.com/Khalid-H634/media-tracker-android/pull/9

---

## Code Review



**Reviewed:** *(pod mate's name)*
**Link to my review:**

Hunter Bammert-Mueller:

https://github.com/Hunterbounty11/media-tracker-android/pull/9/changes/abed0d9a59b168fbeae98e5e460f5c057902576f#r3642650597

Danny King:

https://github.com/DannyKin/media-tracker-android/pull/9/changes/7bb0320b9e37e8cfd5438775675a3ccadd925da2#r3642721038

### What I Looked At

I looked at the MediaDetailViewModel, LibraryViewModel, LibraryScreen, and DefaultMediaRepository 
files. I focused on how both pod mates implemented the optimistic update pattern for adding to 
library, removing items, and toggling favorites.

### What I Noticed

For Hunter's code, I noticed in the addFavorite() function (lines 42-55), there's no error handling
to show the user if something fails. In the addToLibrary() function (lines 77-93), the libraryStatus
only updates after the API succeeds instead of updating immediately, so the button text doesn't 
change until the network request completes.

For Danny's code, I noticed his removeItem() function (lines 51-69) follows the optimistic pattern 
perfectly - the item disappears instantly, a backup is stored, the API call runs in the background,
and rollback happens on failure. His updateStatus() function also updates instantly then rolls back.
I also noticed he added error states in LibraryScreen (lines 91-101) so users see a message when 
something fails.

### Comments I Left
For Hunter: 
LibraryViewModel.kt (line 12): "Should remove this import. It isn't used anyway in the 
LibraryViewModel"

MediaDetailViewModel.kt (lines 42-55) addFavorite(): "should Add a _error StateFlow so you can 
show a Snackbar when an add fails, and reset it when the user tries again."

MediaDetailViewModel.kt (lines 77-93) addToLibrary(): "libraryStatus should update before the 
API call so the button text changes instantly. Store the old status, update immediately, then roll
back on failure."

MediaDetailScreen.kt (lines 189-190): "Good use of the loading state pattern. The spinner gives 
feedback while the background request completes."

DefaultMediaRepository.kt: "Good job on the 409 conflicts. A 409 shouldn't trigger a rollback 
since the item is already added."

For Danny:

LibraryViewModel.kt (lines 51-69) removeItem():"Nice work on the remove functionality. The 
item is removed from the list immediately, with a backup stored and rollback logic in place if the 
network call fails."

LibraryScreen.kt (lines 91-101): "Good job showing the error state to the user. When it fails,
the screen shows a message instead of just being blank."


## One Thing I Understood More Deeply

I understood the update pattern much better after working through it. Update the UI first, make 
the API call second, and roll back only if it fails. This makes the app feel instant and responsive.

For my own code, I implemented this in several places. In addToLibrary(), I store the old status,
update the button text immediately, then call the API. If it fails, I roll back and show an error. 
For remove, the item disappears instantly, then the delete request runs in the background. If it
fails, the item reappears.

I also added the AuthInterceptor to handle authentication for all API requests. It adds the token 
to each request header, except for login and registration, which skip it since they don't have 
a token yet.

For the library screen, I added loading, error, and empty states. A spinner shows while loading, 
an error message with a retry appears on failure, and a message shows when the library is empty. 
I also learned that 409 conflicts shouldn't trigger rollback since the item is already saved.




## One Thing I'm Still Confused About

I ran into an issue that was a 401 error earlier when trying to view the library feed because the
tabs weren't loading anything. It turned out to be an authentication issue on my end. I had bypassed
the login screen earlier and was trying to access the library without an authenticated session.

The API needed a valid token for library requests. It kept rejecting because there was no token to 
send. To fix it, I went back, registered an account, and logged in properly. The library started 
loading correctly right away. Now that I'm logged in, the authentication token gets saved to the 
session repository, and the AuthInterceptor successfully adds it to all the API requests.



## Anything Else *(optional)*




## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
