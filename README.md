# CipherSquadSteganographyTool
Welcome to the Steganography Tool presented by CipherSquad

-This is an encryption app that presents the user with the ability to hide text within an image. The app has both an encode and a decode option. In the encode portion of the app, the user can upload a photo and specify what text they'd like to hide within the image. The text is then hidden in the image, and the image saved to the user's device. The user should be able to send this image to others. The receiver of the encoded image can then upload the image into the decode portion of the app, where it will process the image and reveal the hidden text.
  
The main contributors to this project have been:

  - Ethan Halsall   -   Designed and implemented icon
                    -   Reorganized UI
                    -   Added functionality such that the app only opens image types that are compatible with the steganography library
                    -   Added image previewing on Encode and Decode
  - Matthew Leja    -   Added alert box when user hits encode but did not set a message or did not set an image
                    -   Added a listener to the encode textbox so that when a user hits enter, it hits the encode button.
  - Jamie Christian -   Solved user interface issues presented when soft keyboard opens.
                    -   Added a dialog to inform user when their image has been saved
                    -   Solve user interface issues presented when the hidden text revealed in the decode section is too long for the screen
  - Danielle Bryant -   Created the encode, decode, and main activities
                    -   Created listeners for the buttons, text view, and edit text
                    -   Gave the buttons basic functionality (launching the create activity, open a selection of apps to choose an image)
  - Jonathon Meyer  -   Included the F5 Android Library and compiled its neccesary portions
                    -   Added in the sharing of an encoded image and sharing to the decode portion of the app
                    -   Wrote the code neccesary to handle an incoming image and to process it
                    -   Hooked up the activities to the F5 Android Library
                    -   Added in the wait dialog
                    -   General code cleanup and what not

