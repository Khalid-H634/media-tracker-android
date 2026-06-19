# Week 05 Reflection

**Name:** Khalid Hassan
**Date:** 06-18-2026

---

## Commits This Week



**Link:** https://github.com/Khalid-H634/media-tracker-android/pull/4/commits



## Code Review



**Reviewed:** *(pod mate's name)*
**Link to my review:**

Hunter Bounty
https://github.com/Hunterbounty11/media-tracker-android/pull/5 

Danny King
https://github.com/DannyKin/media-tracker-android/pull/5

### What I Looked At

I went ahead and check on any additonal files or changes that was done to hunter and dannys code


### What I Noticed

i noticed Hunter updated his UserRepository, which looked different from mines
and looked at danny's retrofitInstance file and smart_display

### Comments I Left

I asked Hunter if he figured out any solution for UserRepository file. As well for Danny
on his changes to his smart_display Icon and if he had the same issue with RetroFitInstance.kt.


## One Thing I Understood More Deeply

I think I get how the RegisterScreen connects to the network folder, the screen calls the ViewModel,
which calls the Repository, which makes the API call through UserApiService. Creating the network 
folder helps keep API code organized separately from UI code.


## One Thing I'm Still Confused About

I'm getting Unresolved reference errors in my RetrofitInstance.kt file.
I think the import path might be wrong. I'm also confused about the build.gradle.kts file
what do I need there, since im seeing two of them under Gradle Script folder. 



## Anything Else *(optional)*



## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
