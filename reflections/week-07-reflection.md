# Week 07 Reflection

**Name:** Khalid Hassan
**Date:** 07-02-2026

---

## Commits This Week



**Link:** https://github.com/Khalid-H634/media-tracker-android/pull/7

---

## Code Review



**Reviewed:** *(pod mate's name)* 
**Link to my review:**

Hunter Bammert-Mueller:

https://github.com/Hunterbounty11/media-tracker-android/pull/7

Danny King:

https://github.com/DannyKin/media-tracker-android/pull/7

### What I Looked At

For Hunter, I looked at MediaDetailScreen.kt and SearchResultsViewModel.kt. For Danny, 
I looked at MediaDetailScreen.kt. I focused on the layout structure and ViewModel implementation.

### What I Noticed

Hunter's MediaDetailScreen.kt:
The Box wrapper at line 44 contains the placeholder text and ends before the UI code begins. 
The media variable is defined inside the Box, so the UI below can't access it and won't display.

Hunter's SearchResultsViewModel.kt:
_results.value = fakeSearchResults is redundant since search() and onTypeSelect() clear it
immediately.

Danny's MediaDetailScreen.kt:
Duplicate import for Alignment at lines 8 and 14. Also, viewModel isn't declared before being 
used at line 39.


### Comments I Left
For Hunter, I pointed out that the media variable was trapped inside the Box wrapper and suggested 
moving it outside so the UI could access it. I also mentioned that the fake data assignment in 
SearchResultsViewModel was redundant since the API call in loadNextPage()
already handles fetching real results.

For Danny, I pointed out the duplicate Alignment import and suggested deleting one. I also
noted that viewModel wasn't declared before being used at line 39 and recommended adding val 
viewModel: MediaDetailViewModel = viewModel() at the top of the composable.



## One Thing I Understood More Deeply
This week I understood how the MediaDetailViewModel connects to the MediaDetailScreen. The ViewModel
holds the StateFlow<Media?. which contains the media data, and the Screen collects it using 
collectAsState(). When mediaId is passed from navigation, setMediaId() updates the _mediaId 
StateFlow and loads the corresponding media from FakeMediaRepository.

I also realized that MediaDetailViewModel needs to expose the media as a StateFlow so the UI can 
observe changes. Without collectAsState(), the Screen wouldn't recompose when the media data changes.
The pattern is:

private val _media = MutableStateFlow<Media?>(null)
val media: StateFlow<Media?> = _media.asStateFlow()
val media by viewModel.media.collectAsState()

This make sures the UI updates automatically when the data loads. I also understood that the
ViewModel should handle the data loading logic, not the Screen. The Screen just observes and 
displays the state. This separation is important because it makes the ViewModel testable
and keeps the UI focused on presentation, not data logic.



## One Thing I'm Still Confused About

The build errors show "Unresolved reference 'SEARCH_RESULTS'" in NavGraph.kt, which means I have 
navigation references to a screen that doesn't exist. I'm not sure if I created a search results 
screen or if I'm supposed to use SearchScreen instead. The error also says "Cannot infer type for 
value parameter mediald which looks like a typo where 'mediaId' is misspelled with 
a lowercase l instead of an i.

'm also confused about how to handle the stat grid for different media types. Books should show
page count, movies should show runtime, and shows should show season/episode count. But my Media
model only has publishedYear and genres right now. I don't have pageCount, runtime, seasonCount, 
or episodeCount fields. I'm not sure if the API will return these fields
or if I need to add them to the model myself.

## Anything Else *(optional)*





## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
