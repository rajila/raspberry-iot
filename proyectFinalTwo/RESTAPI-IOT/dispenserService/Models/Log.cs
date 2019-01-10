using System;
using System.ComponentModel.DataAnnotations;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace dispenserService.Models
{
    public class Log
    {
        [Key]
        public int LogId { get; set; }
        public string ThingId { get; set; }
        public string ActionType { get; set; }
        public double AmountDailyFood { get; set; }
        public double AmountBowlFood { get; set; }
        public double AmountDailyWater { get; set; }
        public double AmountBowlWater { get; set; }
        public double CurrentAmountFood { get; set; }
        public double AmountFoodDownloaded { get; set; }
        public double CurrentAmountWater { get; set; }
        public double AmountWaterDownloaded { get; set; }
        public int AngleServoFood { get; set; }
        public int OpeningSecondsFood { get; set; }
        public int AngleServoWater { get; set; }
        public int OpeningSecondsWater { get; set; }
        [DataType(DataType.DateTime)]
        public DateTime CurrentDateTime { get; set; }
    }
}