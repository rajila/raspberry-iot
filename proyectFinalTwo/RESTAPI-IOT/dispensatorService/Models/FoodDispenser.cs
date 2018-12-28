﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace dispensatorService.Models
{
    public class FoodDispenser
    {
        [Key]
        public int FoodDispenserId { get; set; }
        public string ThingId { get; set; }
        public string ActionType { get; set; }
        public double AmountDailyFood { get; set; }
        public double FoodPortion { get; set; }
        public double CurrentAmountFood { get; set; }
        public double AmountFoodDownloaded { get; set; }
        public int AngleServoFood { get; set; }
        public int OpeningSecondsFood { get; set; }
        [DataType(DataType.Date)]
        public DateTime CurrentDate { get; set; }
        [DataType(DataType.Time)]
        public DateTime CurrentTime { get; set; }
    }
}