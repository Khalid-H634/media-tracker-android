# Week 11 Reflection

**Name:** Khalid Hassan
**Date:** 07-30-2026

---

## Commits This Week


**Link:** https://github.com/Khalid-H634/media-tracker-android/pull/10

---

## Code Review


**Reviewed:** *(pod mate's name)*
**Link to my review:**

Hunter Bammert-Mueller:

https://github.com/Hunterbounty11/media-tracker-android/pull/10/changes/734f92d7764d62c5f81a0fc7259a58dd777d16ba#r3688179686

Danny King:

https://github.com/DannyKin/media-tracker-android/pull/10/changes/95ff10639ea85a01be5c3705f29de292a08fc0fa#r3688109060


### What I Looked At

Hunter: I reviewed MediaApiService.kt and PrioritiesViewModel.kt from his Priorities feature. I 
focused on the API endpoints and how the ViewModel loads data.

Danny: I reviewed Quote.kt, MediaDetailViewModel.kt, and QuotesScreen.kt from his Quotes feature. I 
focused on the data model, the save function, and the screen loading logic.
### What I Noticed

Hunter: 
In MediaApiService.kt, the priorities endpoints are set up correctly with GET to fetch the list and
PUT to update. In PrioritiesViewModel.kt (lines 28-31), loadPriorities() uses viewModelScope in the 
init block to load priorities when the screen opens, with a try/catch to prevent crashes if 
the network fails.

Danny:
The Quote data model and API endpoints in Quote.kt and MediaApiService.kt are complete and match the 
server contract. In MediaDetailViewModel.kt, the saveQuote() function uses viewModelScope for the 
createQuote call, which stops the network request if the user leaves the screen. The try/catch 
lets the user know if the quote saved or not.

One issue I noticed: in QuotesScreen.kt, loadQuotes() is defined but never called, so the screen
stays empty.

### Comments I Left

Hunter: 
Pointed out that the priorities endpoints are set up correctly (GET and PUT)
Noted the good use of viewModelScope in the init block and the try/catch for error handling

Danny:

Noted the data model and API endpoints are complete.
Pointed out the good use of viewModelScope for saveQuote() and the try/catch for user feedback
Asked why loadQuotes() is never called and suggested using LaunchedEffect to load data when the 
screen opens

## One Thing I Understood More Deeply

The Write Review feature. At first, I didn't understand how to connect the UI to the API. I had to 
build the ReviewApiService interface with GET and POST endpoints, add it to RetrofitInstance, and 
then update MediaDetailViewModel to fetch real reviews instead of fake ones.

What finally clicked was the flow: WriteReviewScreen collects state from WriteReviewViewModel

When the user taps "Post Review," viewModel.submitReview(mediaId) is called. This creates a 
CreateReviewRequest and calls RetrofitInstance.reviewApiService.createReview()
On success, the ViewModel emits SubmitState.Success and the screen navigates back. Back on 
MediaDetailScreen, MediaDetailViewModel calls GET /reviews and displays the updated list

I struggled with the Retrofit converter errors at first. The server was returning an empty array [] 
but my code expected an object {"reviews": []}. Once I switched back to Response<List<Review>> 
and handled the empty list case, it worked. screen opens

## One Thing I'm Still Confused About

For the "Want To" button. When I click it, the button doesn't show that the item was added to my 
library. I'm using real media IDs from the search results, but the API call might be failing. 
I added logging but still need to check the response code. I think it might be a 401 token 
issue, or the media ID doesn't exist in the library table on the server. The review API works,
but the library API doesn't, and I'm not sure why.

## Anything Else *(optional)*

## Bonus Feature Progress

**What's working:**

StarRatingRow composable. Tapping any star sets the rating to that value.
POST /reviews sends { mediaId, rating, reviewText } to the API and returns a 201.
Reviews list on Media Detail fetches GET /reviews?mediaId={id} and displays real data.

**What's still stubbed, fake, or not started:**

Edit (PUT /reviews/{id}) is not implemented yet.
Delete (DELETE /reviews/{id}) is not implemented yet.
Character counter is still basic but works.


**What I'm blocked on, if anything:**
Nothing major now, the basic flow works.

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
