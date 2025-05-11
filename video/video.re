As a viewer, I want to watch videos, so that I can be entertained and learn new things
{
  Basic Flow {
    (User) 1. the Viewer shall search for videos by keywords or browse categories.
    (User) 2. the Viewer shall select a video to watch.
    (User) 3. the Viewer shall click on video.
    (System) 4. When video is selected, the Platform shall load video and display video information.
    (System) 5. When video is loaded, the Platform shall begin streaming video.
    (User) 6. the Viewer shall watch video.
    (User) 7. the Viewer shall register and login to be a poster.
    (System) 8. When viewer want to register, the Platform shall give an approach to generate a new poseter account.
    (System) 9. When viewer want to login, the Platform shall give response.
  }

//  Alternative Flow {
//    A. At any time, Internet connection fails :
//      To ensure good user experience even with poor connectivity.
//      1. System detects connection issue.
//      2. System pauses video playback.
//      3. System displays connection error message.
//      4. System automatically retries connection.
//        a2. Connection cannot be restored.
//          1. System offers offline download option for premium users.
//  }
}

As a poster, I want to post videos, so that viewers can watch my content
{
  Basic Flow {
    (User) 1. the Poster shall log into their account.
    (User) 2. the Poster shall upload video file.
    (User) 3. the Poster shall add title and description and tags and thumbnail.
    (System) 4. When all information is provided, the Platform shall process video.
    (System) 5. When processing completes, the Platform shall queue video for moderation.
    (System) 6. When video is approved, the Platform shall publish video.
    (System) 7. When video is published, the Platform shall notify subscribers.
    (User) 8. the Poster shall update their video.
    (System) 9. When poster want to update their video, the Platform shall provide platform.
    (System) 10. When poster want to delete their video, the Platform shall delete their video.
    (User) 11. the poster shall like and comment and share video.
    (System) 12. When poster like one video, the Platform shall update video statistics and notify poster.
    (System) 13. When poster comment one video, the Platform shall update video statistics and notify poster.
    (System) 14. When poster share one video, the Platform shall give viewer a link of this video.
    (User) 15. the poster shall watch list of liked videos.
    (System) 16. When poster want to watch list of liked videos, the Platform shall give a list of videos viewers have liked.
    (System) 16. When poster want to logout, the Platform shall response.
  }

//  Alternative Flow {
//    A. At any time, Upload fails :
//      To prevent data loss and ensure successful uploads.
//      1. System detects upload error.
//      2. System saves draft of video information.
//      3. System notifies poster of upload failure.
//      4. System offers retry option with resumable upload.
//        a2. Upload continues to fail after multiple attempts.
//          1. System suggests troubleshooting steps.
//  }
}