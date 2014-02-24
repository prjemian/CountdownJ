
==========
CountdownJ
==========


:summary:   display a clock of time remaining for speakers in a conference session
:author:    Pete R. Jemian
:email:     prjemian@gmail.com
:copyright: 2014, Pete R. Jemian
:license:   GPL3 (see *LICENSE* file)
:git:       https://github.com/prjemian/CountdownJ
:URL:       http://prjemian.github.io/CountdownJ

Motivation
==========

Have you ever been a speaker at a meeting and talked too long? If you'd known 
you only had 3 minutes left, you might have restructured your next remarks.

Perhaps you have been a session organizer and all your speakers take a few extra 
minutes. Your session schedule is just shot to bits.

Here is a tool that will help to keep speakers on schedule by providing a display 
of the time remaining for them to speak. Giving feedback to speaker, sessions 
moderator, and even session attendees, this simple tool has been useful in helping 
conference sessions stick close to the schedule through social engineering. 

Requirements
============

Here is a terse list of the initial display requirements:

* show the time remaining (MM:SS) in big characters on a black background
* show a word or phrase indicating the current phase of the presentation
* no other clutter in the display
* allow for talks of different lengths
* alert speakers and audience when time has run out
* provide audible indicators at phase transitions and in *overtime*

======================  =======================================
phase                   indicator
======================  =======================================
before presentation     white text or blank
starting                1 beep
presentation            green text
change to discussion    2 beeps
discussion / questions  yellow text
time is out             3 beeps
overtime                red text and 3 beeps every interval
======================  =======================================

.. example
    > 05:00      green    PRESENTATION
    > 00:00      yellow   DISCUSSION
    < 00:00      red      OVERTIME
      05:00     1 beep
      00:00     2 beeps
     -01:00     3 beeps  (at each minute)


.. toctree::
   :maxdepth: 1
   :hidden:
   
   contents



This documentation built |today|. 
