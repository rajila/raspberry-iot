using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace dispensatorService.Models
{
    public class WaterDispenser
    {
        [Key]
        public int WaterDispenserId { get; set; }
        public string ThingId { get; set; }
        public string ActionType { get; set; }
        public double LiterWater { get; set; }
        public int MinPercentWater { get; set; }
        public int CurrentPercentWater { get; set; }
        public double LiterWaterDownloaded { get; set; }
        public int AngleServoWater { get; set; }
        public int OpeningSecondsWater { get; set; }
        [DataType(DataType.Date)]
        public DateTime CurrentDate { get; set; }
        [DataType(DataType.Time)]
        public DateTime CurrentTime { get; set; }
    }
}