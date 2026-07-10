# Week 08 Reflection

**Name:** Khalid Hassan
**Date:** 07-09-2026

---

## Commits This Week


**Link:** https://github.com/Khalid-H634/media-tracker-android/pull/8


---

## Code Review



**Reviewed:** *(pod mate's name)*
**Link to my review:**

Hunter Bammert-Mueller:

https://github.com/Hunterbounty11/media-tracker-android/pull/8/changes/dcb90271722d2a30bd74c33d1b94cd59c170f392#r3556095480

Danny King:

https://github.com/DannyKin/media-tracker-android/pull/8/changes/994e7bc51e3baf746bb8f310dcb5fd6bd11ea0a8#r3556222582


### What I Looked At

I looked at MediaDetailViewModel.kt and DefaultMediaRepository.kt to check the Media Detail
screen implementation and API connection.

### What I Noticed

In Hunter's code, I saw the getReviews() function in DefaultMediaRepository.kt returns emptyList()
when the API call fails. This means the user will see "No Reviews" even when there's a network 
error, which can mislead them. In MediaDetailViewModel.kt at lines 19-24, Hunter used a sealed 
class called DetailUiState with three states: Loading, Success, and Error. This is a good pattern 
because it forces the UI to handle all three states explicitly.

For Danny's code, in SearchResultsViewModel.kt at lines 11-15, he used StateFlow for results and
selected type. This allows the UI to observe changes easily.
In MediaApiService.kt at lines 19-23, the media detail endpoint uses media_id with an underscore,
but the library endpoint uses mediaId with camelCase. This inconsistent naming could cause confusion.

In SearchResultsViewModel.kt at lines 19-27, applyFilter() ignores the search query and selected
type. It just returns all results. So typing Dune or tapping Books shows the same list as
tapping All.

### Comments I Left

For Hunter: I pointed out that getReviews() hides errors from the user by returning an empty list.
I suggested using a Result type instead, so the UI can show a proper error message and retry 
button when the API fails.

For Danny: I suggested picking one naming style and using it for all endpoints to avoid confusion.

I suggested filtering fakeSearchResults using currentQuery and selectedType so the search actually 
works.



## One Thing I Understood More Deeply

The app was using hardcoded fake data from FakeMediaRepository.kt. The real data comes from API 
calls using Retrofit. MediaApiService defines the endpoints, RetrofitInstance creates the client,
and the ViewModel calls the service to fetch real data.

In MediaDetailViewModel.kt, I added a sealed class called MediaDetailUiState with three states: 
Loading, Success, and Error. This forces the UI to handle all three cases explicitly.

I also learned how StateFlow and collectAsState() work together. The ViewModel holds the state, 
and the screen collects it. When the state changes, the screen automatically recomposes.

In NavGraph.kt, I added navArgument(mediaId) to pass the ID from search results to the detail 
screen. LaunchedEffect(mediaId) in MediaDetailScreen.kt triggers the ViewModel's setMediaId() 
whenever the mediaId changes, so the screen refreshes correctly.


## One Thing I'm Still Confused About

I noticed that the activity feed items don't show poster images. The ActivityFeedScreen uses 
AsyncImage to load images, but the fake data in FakeMediaRepository has coverUrl = null
for most items.

I'm confused about whether this is just a fake data problem or if the real API data will also 
have missing images. When I call GET /media/{id}, will the response include valid 
coverUrl values? Or do I need to handle missing images differently, like showing a placeholder 
image instead of an emoji?

I also noticed that fakeSearchResults doesn't have coverUrl values at all. This makes me wonder
if the real search results API returns cover images, or if cover images only come from the media
detail endpoint.

## Anything Else *(optional)*



## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
