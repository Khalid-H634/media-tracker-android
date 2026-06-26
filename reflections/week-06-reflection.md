# Week 06 Reflection

**Name:** Khalid Hassan
**Date:** 06-25-2026

---

## Commits This Week


**Link:** https://github.com/Khalid-H634/media-tracker-android/pull/5



## Code Review



**Reviewed:** *(pod mate's name)* 
**Link to my review:**

Hunter Bounty
https://github.com/Hunterbounty11/media-tracker-android/pull/6

Danny King
https://github.com/DannyKin/media-tracker-android/pull/6

### What I Looked At

Hunter: I looked at his networking setup, ApiConstants.kt, RetrofitInstance.kt and LoginRequest.kt. 
To see how he configured his API client compared to mine.

Danny: I looked at his SearchScreen.kt and SearchResultsViewModel.kt
to see how he implemented search functionality.

### What I Noticed

For Hunter's ApiConstants.kt lines 6-7, he uses BuildConfig for client credentials instead of 
hardcoding them, which is more secure. In RetrofitInstance.kt lines 12-14, he uses 
ignoreUnknownKeys = true to prevent crashes if the API adds new fields. In LoginRequest.kt line 6, 
he sets grantType = "password" as a default value.

In Danny's SearchScreen.kt lines 64-84, he uses Column with verticalScroll to display search 
results, which renders all items at once and causes performance issues with larger lists. In 
SearchResultsViewModel.kt lines 19-20, his applyFilter() function sets _results. value =
fakeSearchResults without actually filtering by query or type.

### Comments I Left
For Hunter, I pointed out his good practices with BuildConfig, ignoreUnknownKeys, and the default 
grantType, and asked follow-up questions. For Danny, I suggested switching to LazyColumn and adding
filter logic to applyFilter().



## One Thing I Understood More Deeply

I understood how the search flow works. The SearchScreen collects the user's query and selected 
type, then navigates to SearchResultsScreen with the query as a parameter. The SearchResultsScreen 
then calls the MediaApiService with the query and type to get real results from the API.

I also learned why LazyColumn is needed for displaying search results. When testing with fake 
data, Column with verticalScroll rendered every item at once and became sluggish with many results. 
LazyColumn only renders visible items and reuses views as you scroll, which keeps performance 
smooth even with large result sets.




## One Thing I'm Still Confused About

I'm still confused about why I keep getting "Something went wrong" when trying to register a 
new account. I'm not sure if the issue is with my ApiConstants.kt credentials, my RegisterViewModel,
or something else in the network setup. I've checked Logcat but I'm still not sure what the 
actual error is.



## Anything Else *(optional)*

I resolved the LoginResult import errors, but I'm still getting a "Something went wrong" error when
registering, so theres likely be another issue



## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
