using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace dispenserService.Models
{
    public class WaterDispenser
    {
        [Key]
        public int WaterDispenserId { get; set; }
        public string ThingId { get; set; }
        public string ActionType { get; set; }
        public double MilliLiterWater { get; set; }
        public int MinPercentWater { get; set; }
        public int CurrentMilliLiterWater { get; set; }
        public double MilliLiterWaterDownloaded { get; set; }
        public int AngleServoWater { get; set; }
        public int OpeningSecondsWater { get; set; }
        [DataType(DataType.DateTime)]
        public DateTime CurrentDateTime { get; set; }
    }
}